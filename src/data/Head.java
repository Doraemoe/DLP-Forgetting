package data;

import java.util.HashSet;

public class Head {
	HashSet<Literal> head;
	
	Head() {
		head = new HashSet<Literal>();
	}
	
	public boolean removeHead(Literal lit) {
		return head.remove(lit);
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(Literal s : head) {
			result.append(s).append(";");
		}
		return result.substring(0, result.length() - 1);
	}

	public HashSet<Literal> getHead() {
		return head;
	}

	public boolean addHead(Literal lit) {
		return head.add(lit);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((head == null) ? 0 : head.hashCode());
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
		Head other = (Head) obj;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		return true;
	}
}
