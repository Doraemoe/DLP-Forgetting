package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import data.Literal;
import data.Program;
import data.Rule;

public class Forgetting {

	Read reader;
	Program prog;
	HashSet<Rule> ind = new HashSet<Rule>();
	HashSet<Rule> rel = new HashSet<Rule>();

	HashSet<Rule> WGPPEHead = new HashSet<Rule>();
	HashSet<Rule> WGPPEBody = new HashSet<Rule>();

	HashSet<Rule> SHYPPBody = new HashSet<Rule>();
	HashSet<Rule> SHYPNBody = new HashSet<Rule>();

	HashMap<Literal, HashSet<Rule>> SHYPMap = new HashMap<Literal, HashSet<Rule>>();
	HashSet<Rule> SHYPNRule = new HashSet<Rule>();

	Forgetting() {
		reader = new Read();
		prog = new Program();
	}

	public void readInput(String location) throws IOException {
		//System.out.println(location);

		reader = new Read();
		prog = reader.readFile(location);
		/*
	    for(Rule r : prog.getProgram()) {
	    	System.out.println(r);
	    }
		 */
	}

	public void spilitProgram(String atom) {
		Literal lit = new Literal(false, atom);

		for(Rule rule : prog.getProgram()) {
			if(!rule.containsLiteral(lit)) {
				ind.add(rule);
			}
			else {
				rel.add(rule);
			}
		}
		//System.out.println(ind);
		//System.out.println(rel);
	}

	public void findPairWGPPE(String atom) {
		Literal lit = new Literal(false, atom);
		for(Rule r : rel) {
			if(r.isInHead(lit)) {
				WGPPEHead.add(r);
			}
			else if(r.isInPBody(lit)) {
				WGPPEBody.add(r);
			}
		}

	}

	public void addNewRuleWGPPE(String atom) {
		//System.out.println(WGPPEBody);
		//System.out.println(WGPPEHead);
		Literal lit = new Literal(false, atom);
		for(Rule r1 : WGPPEBody) {
			for(Rule r2 : WGPPEHead) {
				Rule newRule = new Rule();
				newRule.addAllToHead(r1.getHeadSet());
				newRule.addAllToHead(r2.getHeadSet());
				newRule.removeFromHead(lit);
				newRule.addAllToBody(r1.getBodySet());
				newRule.addAllToBody(r2.getBodySet());
				newRule.removeFromBody(lit);
				//System.out.println(newRule);
				//prog.addRule(newRule);
				if(!newRule.isBodyEmpty() && !newRule.isHeadEmpty()) {
					ind.add(newRule);
				}
			}
		}
	}

	public void findPairSHYP(String atom) {
		Literal pLit = new Literal(false, atom);
		Literal nLit = new Literal(true, atom);
		for(Rule r : rel) {
			if(r.isInPBody(pLit)) {
				SHYPPBody.add(r);
			}
			else if(r.isInNBody(nLit)) {
				SHYPNBody.add(r);
			}
		}
		//System.out.println(SHYPPBody);
		//System.out.println(SHYPNBody);
	}

	public void addNewRuleSHYP(String atom) {

		//System.out.println(SHYPPBody);
		//System.out.println(SHYPNBody);

		Literal pLit = new Literal(false, atom);
		Literal nLit = new Literal(true, atom);

		for(Rule r1 : SHYPPBody) {
			for(Rule r2 : SHYPNBody) {
				Rule newRule = new Rule();
				newRule.addAllToHead(r2.getHeadSet());

				newRule.addAllToBody(r1.getBodySet());
				newRule.addAllToBody(r2.getBodySet());
				for(Literal lit : r1.getHeadSet()) {
					Literal newLit = new Literal(true, lit.getAtom());
					newRule.addToBody(newLit);
				}
				newRule.removeFromBody(pLit);
				newRule.removeFromBody(nLit);
				//System.out.println(newRule);
				//prog.addRule(newRule);
				if(!newRule.isBodyEmpty() && !newRule.isHeadEmpty()) {
					ind.add(newRule);
				}

			}
		}
	}

	public void exportToFile(String location, long startTime) throws IOException {
		long endTime=System.currentTimeMillis();
		FileWriter fw = new FileWriter(location);
		for(Rule r : ind) {
			//System.out.println(r);
			fw.write(r.toString());
			fw.write(System.lineSeparator());
		}
		fw.write("Running time: " + (endTime-startTime) + "ms");
		fw.close();
	}

	public void generateProgram(int numOfMinAtom, int numOfMinRule, int numOfMaxHead,
			int numOfMaxBody, int offsetAtom, int offsetRule, int numOfSet, String location) throws IOException {

		Random rnd = new Random();

		for(int set = 0; set < numOfSet; set++) {
			int ruleNum = numOfMinRule + (set * offsetRule);
			int atomNum = numOfMinAtom + (set * offsetAtom);

			for(int i = 0; i < 100; i++) {
				prog = new Program();
				for(int rule = 0; rule < ruleNum; rule++) {
					Rule newRule = new Rule();
					int numOfHead = rnd.nextInt(numOfMaxHead) + 1;
					for(int h = 0; h < numOfHead; h++) {
						Literal lit = new Literal(false, String.valueOf(rnd.nextInt(atomNum)));
						newRule.addToHead(lit);
					}
					int numOfBody = rnd.nextInt(numOfMaxBody + 1);
					int numOfNBody = rnd.nextInt(numOfBody + 1);
					for(int nb = 0; nb < numOfNBody; nb++) {
						Literal lit = new Literal(true, String.valueOf(rnd.nextInt(atomNum)));
						newRule.addToBody(lit);
					}
					for(int pb = 0; pb < numOfBody - numOfNBody; pb++) {
						Literal lit = new Literal(false, String.valueOf(rnd.nextInt(atomNum)));
						newRule.addToBody(lit);
					}
					prog.addRule(newRule);
				}

				File dir = new File(location);
				if (!dir.exists() || !dir.isDirectory()) {
					dir.mkdirs();
				}
				FileWriter fw = new FileWriter(location + File.separatorChar + String.valueOf(atomNum) + "and" + String.valueOf(ruleNum) + "of" + i + ".txt");
				for(Rule r : prog.getProgram()) {
					fw.write(r.toString());
					fw.write(System.lineSeparator());
				}
				fw.close();
			}
		}
	}

	private String readLastLine(File file, String charset) throws IOException {  
		if (!file.exists() || file.isDirectory() || !file.canRead()) {  
			return null;  
		}  
		RandomAccessFile raf = null;  
		try {  
			raf = new RandomAccessFile(file, "r");  
			long len = raf.length();  
			if (len == 0L) {  
				return "";  
			} else {  
				long pos = len - 1;  
				while (pos > 0) {  
					pos--;  
					raf.seek(pos);  
					if (raf.readByte() == '\n') {  
						break;  
					}  
				}  
				if (pos == 0) {  
					raf.seek(0);  
				}  
				byte[] bytes = new byte[(int) (len - pos)];  
				raf.read(bytes);  
				if (charset == null) {  
					return new String(bytes);  
				} else {  
					return new String(bytes, charset);  
				}  
			}  
		} catch (FileNotFoundException e) {  
		} finally {  
			if (raf != null) {  
				try {  
					raf.close();  
				} catch (Exception e2) {  
				}  
			}  
		}  
		return null;  
	}  


	private int getTime(File file) throws IOException {
		String lastLine = readLastLine(file, "utf8");
		lastLine = lastLine.trim();
		String time = lastLine.split(":")[1].trim();
		time = time.substring(0, time.length() - 2);
		return Integer.parseInt(time);
	}


	public void calculateTime(String location) throws IOException {
		String now = new String("empty");
		boolean isFirst = true;
		int totalTime = 0;

		File file = new File(location);
		String[] filelist = file.list(getFileExtensionFilter(".txt"));
		Arrays.sort(filelist);

		FileWriter fw = null;

		for(String s : filelist) {
			File currentFile = new File(location + File.separatorChar + s);
			String atom = s.split("and")[0];
			String rule = s.split("and")[1].split("of")[0];

			if(!now.equals(atom)) {
				if(isFirst) {
					totalTime = getTime(currentFile);
					isFirst = false;
					fw = new FileWriter(location + File.separatorChar + "Result" + atom + "and" + rule + ".txt");
					now = atom;
				}
				else {
					fw.write("Total Time: " + totalTime);
					fw.write(System.lineSeparator());
					fw.write("Average Time: " + totalTime/100);
					fw.close();
					fw = new FileWriter(location + File.separatorChar + "Result" + atom + "and" + rule + ".txt");
					now = atom;
					totalTime = getTime(currentFile);
				}
			}
			else {
				totalTime += getTime(currentFile);
			}
		}
		fw.write("Total Time: " + totalTime);
		fw.write(System.lineSeparator());
		fw.write("Average Time: " + totalTime/100);
		fw.close();
	}

	private static FilenameFilter getFileExtensionFilter(String extension) {
		final String _extension = extension;
		return new FilenameFilter() {
			public boolean accept(File file, String name) {
				boolean ret = name.endsWith(_extension);
				return ret;
			}
		};
	}

	
	private void addToMap(Literal lit, Rule rule) {
		Rule newRule = new Rule();
		
		if(SHYPMap.containsKey(lit)) {
			newRule.addAllToHead(rule.getHeadSet());
			HashSet<Literal> newBody = new HashSet<Literal>(rule.getBodySet());
			newBody.remove(lit);
			newRule.addAllToBody(newBody);
			SHYPMap.get(lit).add(newRule);
		}
		else {
			HashSet<Rule> setRule = new HashSet<Rule>();
			newRule.addAllToHead(rule.getHeadSet());
			HashSet<Literal> newBody = new HashSet<Literal>(rule.getBodySet());
			newBody.remove(lit);
			newRule.addAllToBody(newBody);
			setRule.add(newRule);
			SHYPMap.put(lit, setRule);
		}
	}
	
	public void findRelatedRuleSHYP(String atom) {
		HashSet<Literal> processed = new HashSet<Literal>();
		Literal pLit = new Literal(false, atom);
		
		for(Rule r : rel) {
			if(r.isInPBody(pLit)) {
				SHYPNRule.add(r);
				HashSet<Literal> pSet = r.getBody().getPBody();
				//System.out.println("here?");
				for(Literal l : pSet) {
					Literal nLit = new Literal(true, l.getAtom());
					if(processed.contains(nLit)) {
						continue;
					}
					else {
						processed.add(nLit);
					}
					for(Rule ru : prog.getProgram()) {
						if(ru.getBodySet().contains(nLit)) {
							addToMap(nLit, ru);
						}
					}
				}
			}
		}
		//System.out.println(SHYPMap);
	}
	
	public void addNewRuleSHYPComplate() {
		HashSet<HashSet<Rule>> ruleSet = new HashSet<HashSet<Rule>>();
		boolean zeroFlag = false;
		
		
		for(Rule r : SHYPNRule) {
			HashSet<Literal> pSet = r.getBody().getPBody();
			for(Literal l : pSet) {
				Literal lit = new Literal(true, l.getAtom());
				if(!SHYPMap.containsKey(lit)) {
					zeroFlag = true;
				}
			}
			if(zeroFlag) {
				zeroFlag = false;
				continue;
			}
			for(Literal l : pSet) {
				Literal lit = new Literal(true, l.getAtom());
				ruleSet.add(SHYPMap.get(lit));
			}
			//System.out.println(ruleSet);
			@SuppressWarnings("unchecked")
			HashSet<Rule>[] sets = new HashSet[ruleSet.size()];
			sets = ruleSet.toArray(sets);
			Set<Set<Object>> combRule = cartesianProduct(sets);
			
			for(Set<Object> setRule : combRule) {	
				Rule newRule = new Rule();
				//System.out.println(setRule);
				for(Object rule : setRule) {
					Rule castRule = (Rule)rule;
					newRule.addAllToHead(castRule.getHeadSet());
					newRule.addAllToBody(castRule.getBodySet());
				}
				newRule.addAllToBody(r.getBody().getNBody());
				for(Literal lit : r.getHeadSet()) {
					Literal newLit = new Literal(true, lit.getAtom());
					newRule.addToBody(newLit);
				}
				//System.out.println(newRule);
				if(!newRule.isBodyEmpty() && !newRule.isHeadEmpty()) {
					ind.add(newRule);
				}
			}
		}
	}
	
	public static Set<Set<Object>> cartesianProduct(Set<?>... sets) {
	    if (sets.length < 2)
	        throw new IllegalArgumentException(
	                "Can't have a product of fewer than two sets (got " +
	                sets.length + ")");

	    return _cartesianProduct(0, sets);
	}

	private static Set<Set<Object>> _cartesianProduct(int index, Set<?>... sets) {
	    Set<Set<Object>> ret = new HashSet<Set<Object>>();
	    if (index == sets.length) {
	        ret.add(new HashSet<Object>());
	    } else {
	        for (Object obj : sets[index]) {
	            for (Set<Object> set : _cartesianProduct(index+1, sets)) {
	                set.add(obj);
	                ret.add(set);
	            }
	        }
	    }
	    return ret;
	}

	public static void main(String[] args) throws IOException {
		Forgetting alg = new Forgetting();
		if(args[0].equals("0")) {
			//System.out.println("this");
			alg.generateProgram(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]),
					Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]),
					Integer.parseInt(args[7]), args[8]);
		}
		else if(args[0].equals("1")) {

			alg.readInput(args[1]);
			long startTime=System.currentTimeMillis();
			alg.spilitProgram(args[2]);
			alg.findPairWGPPE(args[2]);
			alg.addNewRuleWGPPE(args[2]);

			//alg.findPairSHYP(args[2]);
			//alg.addNewRuleSHYP(args[2]);

			alg.findRelatedRuleSHYP(args[2]);
			alg.addNewRuleSHYPComplate();
			
			alg.exportToFile(args[3], startTime);
		}
		else if(args[0].equals("2")) {
			alg.calculateTime(args[1]);
		}
	}

}
