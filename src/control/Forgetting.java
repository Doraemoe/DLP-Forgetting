package control;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

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
			fw.write("\r\n");
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
				
				FileWriter fw = new FileWriter(location + "\\" + String.valueOf(atomNum) + "and" + String.valueOf(ruleNum) + "of" + i + ".txt");
				for(Rule r : prog.getProgram()) {
					fw.write(r.toString());
					fw.write("\r\n");
				}
				fw.close();
			}
		}

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
			alg.findPairSHYP(args[2]);
			alg.addNewRuleSHYP(args[2]);
			alg.exportToFile(args[3], startTime);
		}

	}

}
