package data;

public class Literal {
	boolean isNegative;
	String atom;
	
	
	public Literal(boolean isNeg, String atom) {
		isNegative = isNeg;
		this.atom = atom;
	}
	
	@Override
	public String toString() {
		if(isNegative) {
			return "not " + atom;
		}
		else {
			return atom;
		}
	}

	public boolean isNegative() {
		return isNegative;
	}
	
	public void setNegative(boolean isNegative) {
		this.isNegative = isNegative;
	}
	
	public String getAtom() {
		return atom;
	}
	
	public void setAtom(String atom) {
		this.atom = atom;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atom == null) ? 0 : atom.hashCode());
		result = prime * result + (isNegative ? 1231 : 1237);
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
		Literal other = (Literal) obj;
		if (atom == null) {
			if (other.atom != null)
				return false;
		} else if (!atom.equals(other.atom))
			return false;
		if (isNegative != other.isNegative)
			return false;
		return true;
	}

}
