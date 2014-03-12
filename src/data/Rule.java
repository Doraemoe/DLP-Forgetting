package data;

import java.util.HashSet;

public class Rule {
	Head head;
	Body body;
	
	public Rule() {
		head = new Head();
		body = new Body();
	}
	
	@Override
	public String toString() {
		if(head.isEmpty()) {
			return ":- " + body + ".";
		}
		if(!body.isEmpty()) {
			return head + " :- " + body + ".";
		}
		else {
			return head + ".";
		}
	}

	public boolean addToHead(Literal lit) {
		return head.addHead(lit);
	}
	
	public void addAllToHead(HashSet<Literal> set) {
		for(Literal lit : set) {
			head.addHead(lit);
		}
	}
	
	public boolean addToBody(Literal lit) {
		return body.addBody(lit);
	}
	
	public void addAllToBody(HashSet<Literal> set) {
		for(Literal lit : set) {
			body.addBody(lit);
		}
	}
	
	public boolean removeFromHead(Literal lit) {
		return head.removeHead(lit);
	}
	
	public boolean removeFromBody(Literal lit) {
		return body.removeBody(lit);
	}
	
	public boolean isBodyEmpty() {
		return body.isEmpty();
	}
	
	public boolean isHeadEmpty() {
		return head.isEmpty();
	}
	
	public boolean haveCommonHeadAndBody() {
		HashSet<Literal> b = body.getBody();
		HashSet<Literal> h = head.getHead();
		
		HashSet<String> conTest = new HashSet<String>();
		
		if(b.isEmpty() || h.isEmpty()) {
			return false;
		}
		
		for (Literal l : b) {
			String atom = l.getAtom();
			if(!conTest.add(atom)) {
				return true;
			}
		}
		
		for (Literal l : b) {
			Literal tmp = new Literal(false, l.getAtom());
			if(h.contains(tmp)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isMinimalWRT(Rule r) {
		
		if(head.isEmpty()) {
			if(r.getBodySet().containsAll(body.getBody())) {
				return true;
			}
			else {
				return false;
			}
		}
		
		if(body.isEmpty()) {
			if(r.getHeadSet().containsAll(head.getHead())) {
				return true;
			}
			else {
				return false;
			}
		}
		
		if(r.getBodySet().containsAll(body.getBody()) && r.getHeadSet().containsAll(head.getHead())) {
			return true;
		}
		return false;
	}
	
	public boolean containsLiteral(Literal lit) {
		Literal nLit = new Literal(true, lit.getAtom());
		if(body.getBody().contains(lit) || body.getBody().contains(nLit) || head.getHead().contains(lit)) {
			return true;
		}
		return false;
	}
	
	public boolean isInHead(Literal lit) {
		if(head.getHead().contains(lit)) {
			return true;
		}
		return false;
	}
	
	public boolean isInPBody(Literal lit) {
		if(body.getPBody().contains(lit)) {
			return true;
		}
		return false;
	}
	
	public boolean isInNBody(Literal lit) {
		if(body.getNBody().contains(lit)) {
			return true;
		}
		return false;
	}
	
	public Head getHead() {
		return head;
	}
	
	public HashSet<Literal> getHeadSet() {
		return head.getHead();
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public Body getBody() {
		return body;
	}
	
	public HashSet<Literal> getBodySet() {
		return body.getBody();
	}

	public void setBody(Body body) {
		this.body = body;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
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
		Rule other = (Rule) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		return true;
	}
	
	
}
