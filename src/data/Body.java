package data;

import java.util.HashSet;

public class Body {
	HashSet<Literal> pBody;
	HashSet<Literal> nBody;
	HashSet<Literal> body;
	
	Body() {
		pBody = new HashSet<Literal>();
		nBody = new HashSet<Literal>();
		body = new HashSet<Literal>();
	}
	
	public boolean addBody(Literal lit) {
		body.add(lit);
		if(lit.isNegative) {
			return nBody.add(lit);
		}
		else {
			return pBody.add(lit);
		}
	}
	
	public boolean removeBody(Literal lit) {
		body.remove(lit);
		if(lit.isNegative) {
			return nBody.remove(lit);
		}
		else {
			return pBody.remove(lit);
		}
	}
	
	public boolean isEmpty() {
		return body.isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(Literal s : pBody) {
			result.append(s).append(",");
		}
		for(Literal s : nBody) {
			result.append(s).append(",");
		}
		return result.substring(0, result.length() - 1);
	}

	public HashSet<Literal> getPBody() {
		return pBody;
	}

	public HashSet<Literal> getNBody() {
		return nBody;
	}
	
	public HashSet<Literal> getBody() {
		return body;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nBody == null) ? 0 : nBody.hashCode());
		result = prime * result + ((pBody == null) ? 0 : pBody.hashCode());
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
		Body other = (Body) obj;
		if (nBody == null) {
			if (other.nBody != null)
				return false;
		} else if (!nBody.equals(other.nBody))
			return false;
		if (pBody == null) {
			if (other.pBody != null)
				return false;
		} else if (!pBody.equals(other.pBody))
			return false;
		return true;
	}
	
	
}
