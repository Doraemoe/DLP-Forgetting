package control;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import data.Literal;
import data.Program;
import data.Rule;

public class Read {

	Rule rule;
	Program prog;
	Literal lit;

	Read() {
		rule = new Rule();
		prog = new Program();
	}

	public Program readFile(String file) throws IOException {


		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			rule = new Rule();
			String[] sig = line.substring(0, line.length() - 1).split(":-");
			//System.out.println("head: " + sig[0]);
			String[] head = sig[0].split(";");
			for(String h : head) {
				//System.out.println("next is: " + h);
				h = h.trim();
				lit = new Literal(false, h);
				rule.addToHead(lit);
			}
			if (sig.length != 1) {
				//System.out.println("body: " + sig[1]);
				String[] body = sig[1].split(",");
				for(String b : body) {
					b = b.trim();
					if(b.contains("not")) {
						//System.out.println("not atom is: " + b);
						String[] naf = b.split(" ");
						lit = new Literal(true, naf[1]);
						rule.addToBody(lit);
					}
					else {
						lit = new Literal(false, b);
						rule.addToBody(lit);
					}
				}
			}
			//System.out.println(rule);
			if(!prog.isRedundant(rule)) {
				prog.addRule(rule);
			}
		}
		br.close();
		
		
		return prog;
	}


}
