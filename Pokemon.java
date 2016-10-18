//Pokemon Class
//Arshdeep Sidhu
//01-14-16
//This program will be used in the Pokemon Arena program to store/use the Pokemon found in the text file.

import java.util.*;
import java.io.*;

public class Pokemon{ //Pokemon Object Class
	
	private String name; //Variable for name
	private int energy = 50; //Variable for energy (max = 50)
	private final int maxhp; //Variable for maxhp (never changes)
	private int hp; //Variable for hp
	private String type; //Variable for type
	private String resistance; //Variable for resistance
	private String weakness; //Variable for weakness
	private int atkamnt; //Variable for the amount of attacks a pokemon has
	
	private boolean stun = false; //Variable to see if pokemon is stunned
	private boolean disable = false; //Variable to see if pokemon is disabled
	
	private ArrayList<String>atks; //List for all the attacks
	private ArrayList<Integer>costs; //List for all the costs
	private ArrayList<Integer>dmgs; //List for all the damages
	private ArrayList<String>specials; //List for all the specials
	
	public Pokemon(String stats){ //Object creater 
		
		atks = new ArrayList<String>(); //Initializes the arraylists
		costs = new ArrayList<Integer>();
		dmgs = new ArrayList<Integer>();
		specials = new ArrayList<String>();
		
		String [] vitals = stats.split(","); //Takes apart the stats from the textfile
		
		name = vitals[0]; //Passes in name
		maxhp = Integer.parseInt(vitals[1]); //Passes in maxhp
		hp = Integer.parseInt(vitals[1]); //Passes in hp
		type = vitals[2];//Passes in type
		resistance = vitals[3]; //Passes in resistance
		weakness = vitals[4]; //Passes in weakness
		atkamnt = Integer.parseInt(vitals[5]); //Passes in the number of attacks
		
		//The following if statements check how many attacks a pokemon has, then passes in the attack(s), cost(s), damage(s) and special(s)
		if(Integer.parseInt(vitals[5])==1){
			atks.add(vitals[6]);
			costs.add(Integer.parseInt(vitals[7]));
			dmgs.add(Integer.parseInt(vitals[8]));
			specials.add(vitals[9]);
		}
		
		if(Integer.parseInt(vitals[5])==2){
			atks.add(vitals[6]);
			costs.add(Integer.parseInt(vitals[7]));
			dmgs.add(Integer.parseInt(vitals[8]));
			specials.add(vitals[9]);
			atks.add(vitals[10]);
			costs.add(Integer.parseInt(vitals[11]));
			dmgs.add(Integer.parseInt(vitals[12]));
			specials.add(vitals[13]);
		}
		
		if(Integer.parseInt(vitals[5])==3){
			atks.add(vitals[6]);
			costs.add(Integer.parseInt(vitals[7]));
			dmgs.add(Integer.parseInt(vitals[8]));
			specials.add(vitals[9]);
			atks.add(vitals[10]);
			costs.add(Integer.parseInt(vitals[11]));
			dmgs.add(Integer.parseInt(vitals[12]));
			specials.add(vitals[13]);
			atks.add(vitals[14]);
			costs.add(Integer.parseInt(vitals[15]));
			dmgs.add(Integer.parseInt(vitals[16]));
			specials.add(vitals[17]);
			
		}
	//^^^^^^^^^^^^^^^^^^Getting Attacks is done^^^^^^^^^^^^^^^^^^^^
	}
	
	public String getname(){ //Caller for the name variable
		return name;
	}
	
	public int getenergy(){ //Caller for the energy variable
		return energy;
	}
	
	public int getmaxhp(){ //Caller for the maxhp variable
		return maxhp;
	}
	
	public int gethp(){ //Caller for the hp variable
		return hp;
	}
	
	public String gettype(){ //Caller for the type variable
		return type;
	}
	
	public String getresistance(){ //Caller for the resistance variable
		return resistance;
	}
	
	public String getweakness(){ //Caller for the weakness variable
		return weakness;
	}
	
	public ArrayList<String> getatks(){ //Caller for the attack list
		return atks;
	}
	
	public ArrayList<Integer> getdmgs(){ //Caller for the damage list
		return dmgs;
	}
	
	public ArrayList<Integer> getcosts(){ //Caller for the costs list
		return costs;
	}
	
	public ArrayList<String> getspecials(){ //Caller for the special list
		return specials;
	}
	
	public int getatkamnt(){ //Caller for the amount of attacks variable
		return atkamnt;
	}
	
	public void attackcost(int cost){ //Method which takes away energy when an attack is used
		energy-=cost; //Energy minus the cost of the attack 
	}
	
	public void attackdmg(int damage){ //Method which deals damage based on the attack
		hp-=damage; //HP minus the damage of the attack
	}
	
	public void gainhp(int amnt){ //Method whenever HP is being added
		hp+=amnt; //HP plus the desired amount
	}
	
	
	public boolean getstun(){ //Caller for the stun variable
		return stun;
	}
	
	public void getStunned(){ //Method which stuns the pokemon
		stun=true;
	}
	
	public void recoverStun(){ //Method which recovers the pokemon from the stun 
		stun=false;
	}
	
	public void energyRecharge(int amnt){ //Method which is used to restore energy
		energy+=amnt; //Energy plus desired amount
	}
	
	public boolean getdisable(){ //Caller for the disable variable
		return disable;
	}
	
	public void getDisabled(){ //Method which disables the pokemon
		disable=true;
	}
	
	public void recoverDisable(){ //Method which allows the pokemon to recover from the disable
		disable = false;
	}
}