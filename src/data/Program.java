package data;

import java.util.HashSet;

public class Program {

	HashSet<Rule> program;
	
	public Program() {
		program = new HashSet<Rule>();
	}
	
	public void addRule(Rule r) {
		program.add(r);
	}
	
	public boolean isRedundant(Rule ruleExt) {
		if(program.isEmpty()) {
			return false;
		}
		
		if(ruleExt.haveCommonHeadAndBody()){
			return true;
		}
		
		HashSet<Rule> red = new HashSet<Rule>();

		for(Rule ruleInt : program) {
			if(ruleInt.isMinimalWRT(ruleExt)) {
				return true;
			}
			if(ruleExt.isMinimalWRT(ruleInt)) {
				red.add(ruleInt);
			}
			
		}
		//System.out.println(red);
		program.removeAll(red);
		
		return false;
	}

	public HashSet<Rule> getProgram() {
		return program;
	}

	public void setProgram(HashSet<Rule> program) {
		this.program = program;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((program == null) ? 0 : program.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Program other = (Program) obj;
		if (program == null) {
			if (other.program != null)
				return false;
		} else if (!program.equals(other.program))
			return false;
		return true;
	}
	
	
}
