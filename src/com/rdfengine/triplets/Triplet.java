package com.rdfengine.triplets;

public class Triplet {

	int subject;
	int predicate;
	int object;
	int setIndex = 0;
	public Triplet(int subject, int predicate, int object) 
	{
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}

	public Triplet() {
	}

	public int getSubject() {
		return subject;
	}

	public void setSubject(int subject) {
		this.subject = subject;
	}

	public int getPredicate() {
		return predicate;
	}

	public void setPredicate(int predicate) {
		this.predicate = predicate;
	}

	public int getObject() {
		return object;
	}

	public void setObject(int object) {
		this.object = object;
	}
	
	public void setNext(int resource) // sets subject then predicate then object
	{
		if(setIndex > 2)
			{
			System.out.println("ERROR INDEX>2");
			System.exit(0);
			}
		switch(setIndex)
		{
		case 0: setSubject(resource);
		break;
		case 1: setPredicate(resource);
		break;
		case 2: setObject(resource);
		break;
		}
		setIndex++;
	}
	
	public String toString()
	{
	  return subject + "---" + predicate + "-->" + object;
	}
}
