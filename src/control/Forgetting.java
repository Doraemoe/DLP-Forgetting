package control;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

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
		System.out.println(location);
	    
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
				ind.add(newRule);
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
				ind.add(newRule);
			}
		}
	}
	
	public void exportToFile(String location) throws IOException {
		FileWriter fw = new FileWriter(location);
		for(Rule r : ind) {
			//System.out.println(r);
			fw.write(r.toString());
			fw.write("\r\n");
		}
		fw.close();
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Forgetting alg = new Forgetting();
		alg.readInput(args[0]);
		alg.spilitProgram(args[1]);
		alg.findPairWGPPE(args[1]);
		alg.addNewRuleWGPPE(args[1]);
		alg.findPairSHYP(args[1]);
		alg.addNewRuleSHYP(args[1]);
		alg.exportToFile(args[2]);
	}

}
