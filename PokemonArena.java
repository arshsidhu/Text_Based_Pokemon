//Pokemon Arena
//Arshdeep Sidhu
//01-14-16
//This program will create a text based Pokemon game where you choose four pokemon with all unique stats and have to beat a series
//of enemy pokemons all taken from the same file. The Pokemon will be able to attack, retreat and pass. The all also have special abilities,
//weaknesses and resistances.

import java.util.*;
import java.io.*;

public class PokemonArena{ //Pokemon Arena Class 
	
	private static ArrayList<Pokemon>pokes; //List of all pokemon objects create from the file
	private static int numpokes; //Number of pokemon in the file
	private static ArrayList<Pokemon>userpokes; //List of the pokemon the user selects
	private static ArrayList<Pokemon>badguys; //List of the bad guys which the user has to defeat
	private static int badguynum=0; //Keeps track of which bad guy the user is battling
	private static int chosenpoke; //Keeps track of the user pokemon which has been selected for battle
	private static int turn=1; //Turn varies from 1 to -1, signifies whos turn it is, 1 being good guys and -1 being bad guys
	private static int roundcounter=0; //Keeps track of how many rounds past - used for energy
	private static int deadgoodguys=0; //Keeps track of how many of the userpokes have fainted
	private static int deadbadguys=0; //Keeps track of how many of the bad guys were defeated

//------------------------------------Main Method-------------------------------------------------

	public static void main(String[]args) throws IOException{
		pokemoncreator(); //Calls the function which creates the pokemon objects
		display(); //Calls the functions which displays all the pokemon
		choose(); //Calls the function allowing the user to choose their desired pokemon
		select(); //Starts the battle when you select the battling pokemon
	}
	
//------------------------------------Simple Random Method----------------------------------------------------
	//Use to get a random number instead of setting up "Random rand ..." every time
	public static int random(int range){ //Takes in thee range of the random number
		Random rand = new Random();
		int num = rand.nextInt(range);
		return num; //Returns the number at the end
	}
	
//-----------------------------------Pokemon Creator Method--------------------------------------------------------
	//Used to create the objects and read the file
	public static void pokemoncreator() throws IOException{ //Throws exception to read file
		pokes = new ArrayList<Pokemon>(); //Initializes list of pokemon
		Scanner inFile = new Scanner(new BufferedReader(new FileReader("pokemon.txt"))); //Reads file
		numpokes = Integer.parseInt(inFile.nextLine()); //Gets the total number of pokemones
		for (int i = 0; i< numpokes; i++){
			pokes.add(new Pokemon(inFile.nextLine())); //Passes the information through the pokemon object, then stores them in a list
		}
	}

//-------------------------------------Display Method------------------------------------------------------
	//Displays the pokemon
	public static void display(){
		System.out.printf("%28s|%-4s|%-9s|%-10s|%-9s|\n" , " ", "HP", "Type", "Resistance", "Weakness" );
		for(int i=0; i<pokes.size(); i++){
			System.out.printf("%-2d. Pick me! I'm %-11s|%-4s|%-9s|%-10s|%-9s|\n" , i+1 , pokes.get(i).getname(), pokes.get(i).gethp(), pokes.get(i).gettype(), pokes.get(i).getresistance(), pokes.get(i).getweakness());
		} // Prints out the name, hp, type, resistance and weakness. String formatting to make it look nice and clean
	}
	
//------------------------------------Chooseing Method----------------------------------------------------------------------	
	//Allows the user to pick their four pokemon
	public static void choose(){
		Scanner kb = new Scanner(System.in); //Scanner
		userpokes = new ArrayList<Pokemon>(); //Initializes the user pokemon list
		for(int i=0; i<4; i++){ //Allow to pick 4 pokemon
			System.out.println("----------------------"); //Dashed lines to make it look cleaner
			System.out.println("Choose your pokemon!:");
			System.out.println("----------------------\n");
			int choice = kb.nextInt(); //Allows to choose pokemon by a number indicated
			userpokes.add(pokes.get(choice-1)); //Stores the chosen pokemon in an arraylist
			pokes.remove(pokes.get(choice-1)); //Removes the chosen pokemon from the original pokemon list
			display(); //Displays the rest of the pokemon to choose from
		}
		badguys = new ArrayList<Pokemon>(); //Initializes badguy list
		for(int i=0; i<pokes.size(); i++){
			badguys.add(pokes.get(i)); //Any pokemon that was not picked automatically turns into a bad guy - adds them to the badguy list
		}
		Collections.shuffle(badguys); //Shuffles the list to randomize the order
	}
	
//---------------------------------Random Turn Selector Method------------------------------------------------
	//Used to randomly determine who goes first in the beginning of the battle and when a bad guy is defeated 
	public static void randomturn(){
		
		int turn = random(2); //Uses random method to determine whos turn it is
		if(turn==0){ //if 0, it is the user's turn
			System.out.println("-----------------"); //Dashes to make it cleaner
			System.out.println("It is your turn!");
			System.out.println("-----------------\n");
			turn=1; //Changes numerical value for the turn to good guy turn (1)
			moveselect(); //Starts the good guy turn
		}
		if(turn==1){ //if 1, it is the bad guy's turn
			System.out.println("------------------------"); // Dash's to make it cleaner
			System.out.println("It is the enemy's turn!");
			System.out.println("------------------------\n");
			turn=-1; //Changes numerical value for the turn to bad guy turn (-1)
			badguymove(); //Starts the bad guy turn
		}
	}
	
//-----------------------------End of Round Energy Restore Method--------------------------------------------------------------
	//Because of my overcomplicated style of code, I keep track of the round whenever the user or bad guy starts their turn. 
	//Once both of them had a turn (even if they did not attack) the round counter will be at 2, signify the end of one round.
	//Therefore, whenever we move from the badguy attack method to the good guy attack method of any instance involving the two
	//we need to check if the round counter is at 2 so we can restore energy. This method appears very often in my code, often 
	//followed by "moveselect" or "badguymove".
	public static void EORenergy(){
		if(roundcounter/2==1){ //Checks if roundcounter is at two 
			int eamnt=0; //eamnt = energy amount that is going to be gained
			for(int i=0; i<userpokes.size();i++){
				eamnt = 50 - userpokes.get(i).getenergy(); //Checking how much energy is missing
				if(eamnt>10){ //If it is greater than 10, keeps the amount at 10
					eamnt=10;
				}
				System.out.println("----------------------------------------------------");
				userpokes.get(i).energyRecharge(eamnt); //Uses object to add more energy on depending on the eamnt
				System.out.printf("%s gained %d energy because the round ended!\n" , userpokes.get(i).getname(), eamnt);
			}
			eamnt= 50 - badguys.get(badguynum).getenergy(); //Finds the eamnt for the badguy pokemon as well
			if(eamnt>10){
				eamnt=10;
			}
			badguys.get(badguynum).energyRecharge(eamnt); //Uses the object to add more energy on as well
			System.out.println("----------------------------------------------------");
			System.out.printf("%s gained %d energy because the round ended!\n" , badguys.get(badguynum).getname(), eamnt);
			System.out.println("----------------------------------------------------\n");
		}
		roundcounter=0; //Resets the round counter so I can start keeping track of the rounds all over again
	}
	
//-------------------------------------Select Method---------------------------------------------------------
	//This method starts the recursion of my program. It also allows me to choose my pokemon which i want battling first
	public static void select(){ 
				
		Scanner kb = new Scanner(System.in); //Scanner 
		System.out.println("------------------------------------"); //Dashed lines for clean look
		System.out.printf("You are battling %s (%d hp)\n", badguys.get(badguynum).getname(), badguys.get(badguynum).gethp()); //Displaying the enemy, with basic name and hp stats
		for(int i = 0; i < userpokes.size(); i++){ //Loops to display user pokemon to choose from
			System.out.printf("%-1d. %-11s (HP: %d Energy: %d)\n", i+1, userpokes.get(i).getname(), userpokes.get(i).gethp(), userpokes.get(i).getenergy());
		}   // Displays their name, hp and energy
		System.out.println("Choose your pokemon!");
		System.out.println("------------------------------------");
		chosenpoke = kb.nextInt()-1; //Scanner keeps track of the chosen pokemon
		System.out.println("-------------------------"); //Dashed line for clean look
		System.out.printf("%s! I choose you!\n" , userpokes.get(chosenpoke).getname()); //Says iconic pokemon quote
		System.out.println("-------------------------\n");
		randomturn(); //Calls Random turn method to see who goes first
	}
	
//---------------------------Retreat Method-------------------------------------------------------------------
	//Used for when the user decided to retreat and switch pokemon
	//Very similar to "Select" method, however has different dialogue and takes up turn
	public static void retreat(){
	
		Scanner kb = new Scanner(System.in); //Scanner
		System.out.println("-------------------------------------"); //Dashed lines for clean look
		System.out.printf("%s Retreated!\nYou are battling %s (%d hp)\n", userpokes.get(chosenpoke).getname(), badguys.get(badguynum).getname(), badguys.get(badguynum).gethp()); //Showing the retreating pokemons name, and updating the user on which pokemon he is battlign
		for(int i = 0; i < userpokes.size(); i++){ //Loops through my other options
			System.out.printf("%-1d. %-11s (HP: %d Energy: %d)\n", i+1, userpokes.get(i).getname(), userpokes.get(i).gethp(), userpokes.get(i).getenergy() );
		} //Shows options with name, hp and energy
		System.out.println("Choose your next pokemon!");
		System.out.println("-------------------------------------\n");
		chosenpoke = kb.nextInt()-1; //Scanner updates the chosen pokemon
		System.out.println("-------------------"); //Dashed lines for clean look
		System.out.printf("%s! I choose you!\n" , userpokes.get(chosenpoke).getname()); //Iconic saying
		System.out.println("-------------------\n");
		System.out.println("------------------------");
		System.out.println("It is the enemy's turn!"); //Automatically takes up your turn switching pokemon
		System.out.println("------------------------\n");
		turn=-1; //Switches numerical value for the turn to bad guy
		badguymove(); //Starts bad guy move.
	}
	
//-----------------------------------------Move Selction Method---------------------------------------------------------
	//This method allows you to choose whether you would like to attack, retreat or pass.
	//It will also let you know if you are stunned and are unable to attack
	public static void moveselect(){
				
		roundcounter+=1; //User has to go through this method no matter what, so it makes sense to consider as part of a round whenever it is called
		Scanner kb = new Scanner(System.in); //Scanner
		stuncheck(userpokes.get(chosenpoke)); //Checks if the pokemon is stunned
		System.out.println("------------------"); //Dashed lines for clean look
		System.out.println("Would you like to:\n1. Attack\n2. Retreat\n3. Pass"); //Gives the user options if the pokemon is not stunned
		System.out.println("------------------\n");
		int movechoice = kb.nextInt();
		if(movechoice==1){
			goodguyatk(); //If you would like to attack, starts the good guy attack method
		}
		if(movechoice==2){
			retreat(); //If you would like to retreat, starts the retreat method
		}
		if(movechoice==3){
			System.out.println("---------------------------"); //Dashed lines for clean look
			System.out.println("You have decided to pass!\nIt is the enemy's turn!"); //Identifies the pass
			System.out.println("---------------------------\n");
			turn=-1; //If you pass, changes turn's numerical value to bad guy turn and starts the bad guy move method
			badguymove();
		}
	}
	
//------------------------------------------Bad Guy Defeat---------------------------------------------------------
	//Used whenever a bad guy pokemon is beaten in battle.
	//Also used to give 20 hp once a battle is complete.
	public static void badguydefeat(){
		deadbadguys+=1; //Once a bad guy is defeated, it is added to the coutner
		if(deadbadguys==badguys.size()){ //Checks if all bad guys are defeated
			endWinner(); //Runs the final method to crown the user Trainer Supreme
			System.exit(1);	//Exits the rest of the program since you beat the game
		}
		Scanner kb = new Scanner(System.in); //Scanner
		System.out.println("----------------------"); //Dashed line for good looks
		System.out.printf("You have defeated %s!\nYou will be battling %s (HP: %d) next!\n" , badguys.get(badguynum).getname() , badguys.get(badguynum+1).getname(), badguys.get(badguynum+1).gethp()); //Names who the user has beaten and shows them who they are battling next
		System.out.println("----------------------\n");
		badguynum+=1; //Moves on to the next bad guy pokemon on the list
		for(int i=0; i< userpokes.size() ; i++){ //Loops through the list of user pokemon
			if(userpokes.get(i).getdisable()==true){ //Uses the object to check is they are disabled
				userpokes.get(i).recoverDisable(); //Makes the pokemon recover from the disable
			}
			int amnt = userpokes.get(i).getmaxhp()-userpokes.get(i).gethp(); //Amnt = amount of hp going to be gained
			if(amnt<20){ //If the maxhp - the current hp is less than 20
				userpokes.get(i).gainhp(amnt); // the hp goes back to max
			}
			else{ //If it  is greater than than 20
				userpokes.get(i).gainhp(20); //You simply add 20 hp
			}
			System.out.println("----------------------------------------------------");
			System.out.printf("%s gained %d HP because it of the end of the battle\n", userpokes.get(i).getname(), amnt);
			System.out.println("----------------------------------------------------\n");
		}
		randomturn(); //Since it is the end of the battle, a random person will start.
	}
	
//------------------------------------User Pokemon Fainted Method------------------------------------------------------------------	
	//Used when a user pokemon is defeated in battle
	public static void goodguydefeat(){
		deadgoodguys+=1; //Keeps track of how many poke the user has lost
		if(deadgoodguys==4){ //Keeps track of if all the users' pokemon are fainted
			endLoser(); //If they are, run final losing method
			System.exit(1); //exit the program
		}
		System.out.println("----------------------"); //Dashed line for clean look
		System.out.printf("%s has been defeated!\nChoose another pokemon!\n" , userpokes.get(chosenpoke).getname()); //Says who is defeated
		System.out.println("----------------------\n");
		userpokes.remove(chosenpoke); //Removes the fainted pokemon from the list
		turn=1; //Since the battle is not done until all four pokemon are dead, it is still the users' turn
		select(); //Goes back to select, to select another pokemon to use in battle
	}

//------------------------------------Ending if you Win Method------------------------------------------------------------------
	//Used when you win
	public static void endWinner(){
		System.out.println("------------------------------------");//Dashed line for clean look
		System.out.println("Congratulations!\n You have defeated all the enemy pokemon!\nYou are officially crowned Trainer Supreme!"); //Crowns them a victor 
		System.out.println("------------------------------------\n");
	}
	
//--------------------------------------Ending if you Lose Method----------------------------------------------------------------
	//used when you lose
	public static void endLoser(){
		System.out.println("-------------------------------------------------------------------------"); //Dashed line for clean look
		System.out.println("Unfortunately all of your pokemon have fainted ... better luck next time!"); //Gives the user the rough news
		System.out.println("-------------------------------------------------------------------------\n");
	}
	
//---------------------------------------Energy Check Method---------------------------------------------------------------
	//Used to check if pokemon have enough energy too attack
	public static boolean energycheck(Pokemon poke){ //Returns true or false
		Collections.sort(poke.getcosts()); //Sorts the costs from lowest to highest order
		if(poke.getenergy()>poke.getcosts().get(0)){ //if the pokemon has enough energy for the lowest costing attack:
			return true; //Return true
		}
		else{
			return false; //if not return false
		}
	}
	
//-------------------------------------------Checking for Resistance Method----------------------------------------------------------------
	//Used to see how much damage a certain pokemon does to another
	public static boolean resistancecheck(String atktype, String resistance){ //Pass in the attacking pokemon's type and the defending pokemon's resistance
		if(atktype.equals(resistance)){ //If they equal the same
			return true; //There is resistance
		}
		else{
			return false; //If not there is not
		}
	} 
	
//-----------------------------------------Checking for Weakness Method-------------------------------------------------------------------	
	//Used to see how much damage a certain pokemon does to another
	public static boolean weaknesscheck(String atktype, String weakness){ //Pass in the attacking pokemon's type and the defending pokemon's weakness
		if(atktype.equals(weakness)){ //If there is a weakness
			return true; //return true
		}
		else{
			return false; // if not, return false
		}
	}
	
//---------------------------------------------Stun Checking Method--------------------------------------------------------------
	//Checks to see if Pokemon is stunned
	public static void stuncheck(Pokemon poke){ //Pass in the potentially stunned pokemon
				
		if(poke.getstun()==true){ //If it is stunned
			System.out.println("---------------------------------------"); //Dashed lines for clean look
			System.out.printf("%s is stunned, it's turn is skipped!\n", poke.getname()); //Identifies the stunned pokemon
			System.out.println("---------------------------------------\n");
			poke.recoverStun(); //After the turn is skipped, unstuns the pokemon using the object
			if(turn==1){ //Uses the turn to determine the next turn
				EORenergy(); //Checks to see if a round has gone by
				badguymove(); //If it was the user's pokemon who got stunned, its the bad guys tunr
			}
			if(turn==-1){
				EORenergy(); //Checks for see if a round has gone by
				moveselect(); //If it was the bad guy's turn, its now the user's turn
			}
		}
	}
	
//----------------------------------------Stun Method------------------------------------------------------------------	
	//Used to determine if the stun will go through, and if it does, actually stun the pokemon
	public static void stun(String special, Pokemon poke){ //Pass in the special of the attacking pokemon, and the defending pokemon
		if(special.equals("stun")){ //checks to make sure the special is actually stun
			int chance = random(2); //Randomly finds the chance of getting stuns(50/50)
			if(chance == 0){ //If it comes out as 0, the pokemon is stunned
				System.out.printf("%s got stunned!\n", poke.getname()); //Names who got stunned
				poke.getStunned(); //uses object to stun the pokemore
			}
		}
	}
	
//---------------------------------------Disable Method-----------------------------------------------------------------
	//used for when the special disable is used
	public static void disable(String special, Pokemon poke){ //Pass in the attacking pokemon's special and the defending pokemon
		if(special.equals("disable")){ //Checks if the special is actually disables
			System.out.println("------------------"); //Dashed line for clean look
			System.out.printf("%s got disabled\n", poke.getname()); //Says who got disabled
			System.out.println("------------------\n");
			poke.getDisabled(); //uses object to disable pokemon
		}
	}
//---------------------------------------Wild Card Method--------------------------------------------------------------------------	
	//Used for when the wild card special is present
	public static boolean wildcard(){
		int atk = random(2); //Uses random to get two options
		if(atk==0){
			return true; //If 0 the attack goes through
		}
		else{
			return false; //If 1 the attack doesn't go through
		}
	}
	
//--------------------------------------Recharge Amount Method--------------------------------------------------------------	
	//Calculates the appropriate amount of energy to gain from the recharge special
	public static int rechargeAmnt(int energy){
		int amnt = 20;
		if(50-energy<20){ //If your max energy - your current energy is lower than 20, you recharge that amount instead of 20
			amnt = 50-energy;
		}
		return amnt; //Returns amount to use later
	}
	
//----------------------------------------Wild Storm Attack Method-----------------------------------------------------------------
	//Very Similar to the GoodGuyAttack Method. Used when a pokemon has the special "wild storm." Uses recursion to keep on attacking
	
	public static void wildstorm(Pokemon atkpoke, Pokemon dmgpoke, String atk, int damage, int cost, String type, String resistance, String weakness){
		//Pass in the attacking pokemon, defending pokemon, the attack of the attacker, the damage of the attack, cost of the attack, type of attacking pokemon, resistance and weakness of the defending pokemon
		
		int choice = random(2); //Uses random to determine if the wild storm is successful
		if(choice==0){ //If the choice is 0 it is not successfull
			System.out.println("-------------------------------"); //Dashed line for clean look
			System.out.println("Wild Storm was not successful!\nIt's the enemy's turn."); //Tells the user the attack wasn't successful
			System.out.println("-------------------------------\n"); 
			if(userpokes.contains(atkpoke)){ //Depending on who used the attack -- checking if a userpoke used the attack
				EORenergy(); //Checks if it the end of round energy needs to be restored
				badguymove(); //Goes to the badguy move method
			}
			if(badguys.contains(atkpoke)){ //checking if a baddguy used wild storm
				EORenergy(); //Checks if the end of round energy needs to be restored
				moveselect();
			}
		}
		else{ //Else the option is 1, the attack was successfull.
			System.out.println("-------------------------------");
			System.out.printf("Wild Storm was successful!\n%s used %s!\n", atkpoke.getname(), atk); //Tells user the attack is successful
			
			if(resistancecheck(type,resistance)==true){ //Checks to see if the defending pokemon has a resistance
				System.out.println("The attack was not very effective ...");
				damage*=0.5; //If so the damage is cut in half
			}
			if(weaknesscheck(type, weakness)==true){ //checks to see if the defending pokemon has a weakness
				System.out.println("The attack was super effective!");
				damage*=2; //If so the damage is doubled
			}
			
			atkpoke.attackcost(cost); //Uses the object to take away energy
			dmgpoke.attackdmg(damage); //and deal dmg
			
			int hp = dmgpoke.gethp(); //Variable to keep track of how much health the defending pokemon has
			
			if(hp<0){
				hp = 0; //Makes sure the health does not go into the negatives
			}

			System.out.printf("The attack did %d damage!\n%s has %d HP left\n" ,damage , dmgpoke.getname(), hp); //Displays defending pokemons stats after the attacak
			System.out.println("-------------------------------\n");
			
			if(dmgpoke.gethp()==0 || dmgpoke.gethp()<0){ //If the pokemon is defeated, move on to the next turn
				if(userpokes.contains(atkpoke)){ //Depending on who used the attack -- checking if a userpoke used the attack
					EORenergy(); //Checks if it the end of round energy needs to be restored
					badguymove(); //Goes to the badguy move method
				}
				if(badguys.contains(atkpoke)){ //checking if a baddguy used wild storm
					EORenergy(); //Checks if the end of round energy needs to be restored
					moveselect();
				}
			}
			
			else{
				wildstorm(atkpoke, dmgpoke, atk, damage, cost, type, resistance, weakness); //if the pokemon is not dead, runs wild storm again for a potential attack
			}
		}
	}
	
//----------------------------------------User Pokemon Attack Method-------------------------------------------------------------------------	
	//Used for when the user has to attack
	public static void goodguyatk(){
				
		Scanner kb = new Scanner(System.in); //Scanner
		if(energycheck(userpokes.get(chosenpoke))==false){ //Checks if you have enough energy for an attack
			System.out.println("------------------------------------------"); //Dashed line for clean look
			System.out.println("You do not have enough energy to attack!\nEither pass or choose another pokemon.");
			System.out.println("------------------------------------------");
			moveselect(); //Brings them back to the move select so they can choose if they want to retreat or pass
		}
		System.out.println("---------------------------------------");
		for(int i=0; i<userpokes.get(chosenpoke).getatks().size(); i++){ //Loops through the attacks
				System.out.printf("%-1d. %-11s (Damage: %-2d Cost: %-2d Special: %-9s) \n"	 , i+1, userpokes.get(chosenpoke).getatks().get(i), userpokes.get(chosenpoke).getdmgs().get(i), userpokes.get(chosenpoke).getcosts().get(i), userpokes.get(chosenpoke).getspecials().get(i));
			} //Displays the damage, cost and special of each attack
		System.out.println("Choose your attack!"); 
		System.out.println("---------------------------------------\n");
		int atkchoice = kb.nextInt(); //Keeps track of the attack choice
		
		int damage=0; //Variable for damage
		int cost=0; //Variable for cost
		String atk=""; //Variable for attack name
		String special=""; //Variable for special name 
		
		//The following if statements first check how many attacks there are in the pokemon
		//Then based off your attack choice, replaces damage, cost, atk and special with the chosen stats
		
		if(userpokes.get(chosenpoke).getatkamnt()==1){ 
			if(atkchoice==1){
				
				atk = userpokes.get(chosenpoke).getatks().get(0);
				damage = userpokes.get(chosenpoke).getdmgs().get(0);
				cost = userpokes.get(chosenpoke).getcosts().get(0);
				special = userpokes.get(chosenpoke).getspecials().get(0);
				
			}
		}
		
		if(userpokes.get(chosenpoke).getatkamnt()==2){
			if(atkchoice==1){
				
				atk = userpokes.get(chosenpoke).getatks().get(0);
				damage = userpokes.get(chosenpoke).getdmgs().get(0);
				cost = userpokes.get(chosenpoke).getcosts().get(0);
				special = userpokes.get(chosenpoke).getspecials().get(0);

			}
			
			if(atkchoice==2){
				
				atk = userpokes.get(chosenpoke).getatks().get(1);
				damage = userpokes.get(chosenpoke).getdmgs().get(1);
				cost = userpokes.get(chosenpoke).getcosts().get(1);
				special = userpokes.get(chosenpoke).getspecials().get(1);
				

			}
		}
		
		if(userpokes.get(chosenpoke).getatkamnt()==3){
			
			if(atkchoice==1){
				
				atk = userpokes.get(chosenpoke).getatks().get(0);
				damage = userpokes.get(chosenpoke).getdmgs().get(0);
				cost = userpokes.get(chosenpoke).getcosts().get(0);
				special = userpokes.get(chosenpoke).getspecials().get(0);
				
			}
			
			if(atkchoice==2){
				
				atk = userpokes.get(chosenpoke).getatks().get(1);
				damage = userpokes.get(chosenpoke).getdmgs().get(1);
				cost = userpokes.get(chosenpoke).getcosts().get(1);
				special = userpokes.get(chosenpoke).getspecials().get(1);
				
			}
			
			if(atkchoice==3){
				
				atk = userpokes.get(chosenpoke).getatks().get(2);
				damage = userpokes.get(chosenpoke).getdmgs().get(2);
				cost = userpokes.get(chosenpoke).getcosts().get(2);
				special = userpokes.get(chosenpoke).getspecials().get(2);
				
			}
		}
		
		//^^^^^^^^^The attack selections stop here^^^^^^^^^^^^^^^^^^^
		
		if(special.equals("wild card")){ //Checks if the special is wild card
			if (wildcard()==true){ //Runs the wild card function to see if you can attack or not -- if true
				System.out.println("-------------------------------"); //Dashed line for clean look
				System.out.println("Your attack was not successful!\nIt's the enemy's turn."); //The attack is not successful
				System.out.println("-------------------------------\n");
				EORenergy(); //Checks if the end of round energy needs recharging
				badguymove(); //Runs the bad guy attack method
			}
		}
		
		String type = userpokes.get(chosenpoke).gettype(); //Gets type of user pokemon
		String resistance = badguys.get(badguynum).getresistance(); //Gets the resistance and weakness of the badguy you are battling
		String weakness = badguys.get(badguynum).getweakness();
		
		if(special.equals("wild storm")){ //if the special is "wild storm" runs the wild storm method instead of the normal attack method
			wildstorm(userpokes.get(chosenpoke),badguys.get(badguynum), atk, damage, cost, type, resistance, weakness);
		}
		System.out.println("--------------------");
		System.out.printf("%s used %s!\n", userpokes.get(chosenpoke).getname(), atk); //Names the pokemon and it's attack.
		
		if(special.equals("recharge")){ //If the special is recharge
			System.out.printf("%s gained %d energy because of Recharge!\n", userpokes.get(chosenpoke).getname(), rechargeAmnt(userpokes.get(chosenpoke).getenergy()));
			userpokes.get(chosenpoke).energyRecharge(rechargeAmnt(userpokes.get(chosenpoke).getenergy()));
			//Uses object and rechargeAmnt method to give pokemon more energy
		}
		
		if(resistancecheck(type,resistance)==true){ //Checks to see if the defending pokemon has a resistance
			System.out.println("The attack was not very effective ...");
			damage*=0.5; //If so the damage is cut in half
		}
		if(weaknesscheck(type, weakness)==true){ //Checks to see if the defending pokemon has a weakness
			System.out.println("The attack was super effective!");
			damage*=2; //If so the damage is doubled
		}
		
		if(userpokes.get(chosenpoke).getdisable()==true){ //Checks if the pokemon is disabled
			damage-=10; //If it is, takes away 10 damage
			if(damage<0){ //Makes sure the damage does not go into the negatives
				damage=0;
			}
		}
		
		stun(special, badguys.get(badguynum)); //Checks if the special is stun
		
		disable(special, badguys.get(badguynum)); //Checks if the special is disable
		
		userpokes.get(chosenpoke).attackcost(cost); //Uses object to take away cost from userpoke
		badguys.get(badguynum).attackdmg(damage); //Uses object to inflict damage to badguy
		
		int hp = badguys.get(badguynum).gethp(); //variable for badguy hp
		
		if(hp<0){ //Makes sure the hp does no go under 0
			hp = 0;
		}
		
		System.out.printf("The attack did %d damage!\n%s has %d HP left\n" ,damage , badguys.get(badguynum).getname(), hp); //Gives the badguy stats after the attack
		System.out.println("--------------------\n");
		if(badguys.get(badguynum).gethp()==0 || badguys.get(badguynum).gethp()<0){ //Checks if the badguy is dead
			badguydefeat(); //Runes the badguy defeat method
		}
		
		else{ 
			System.out.println("------------------------");//Dashed like for clean look
			System.out.println("It is the enemy's turn!");
			System.out.println("------------------------\n");
			turn=-1; //Turn the numerical value of the turn to the badguy turn
			EORenergy(); //Checks for end of round energy recharge
			badguymove(); //Runes badguy move
		}
	}
	
//---------------------------------------Bad Guy Move Method--------------------------------------------------------------------------	
	//Used to determine what the badguy does
	public static void badguymove(){
				
		roundcounter+=1; //Since the badguy always starts with this function, it adds to the round counter
		stuncheck(badguys.get(badguynum)); //Checks if the badguy is stunned
		Collections.sort(badguys.get(badguynum).getcosts()); //Sorts the attack costs from lowest to highest 
		if (badguys.get(badguynum).getenergy()>=badguys.get(badguynum).getcosts().get(0)){ //If you can afford an attack
			badguyatks(); //Go to bad guy attacks
		}
		else{ //Else badguy has to pass  
			System.out.println("-------------------------------"); //Dashed line for clean look 
			System.out.println("The enemy has decided to pass!\nIt is your turn!\n");
			System.out.println("-------------------------------");
			turn = 1; //Numerical value for turn is changed to good guy
			EORenergy(); //Checks for end of round energy recharge
			moveselect(); //Moves on to user turn
		}
	}
	
//--------------------------------------------Bad Guy Attack Method---------------------------------------------------------------------	
	//Used when the badguy can attack
	public static void badguyatks(){
					
		int n = badguys.get(badguynum).getcosts().size(); //Variable for how many attacks the badguy has
		int atk = random(n); //uses random method to determine which attack
		
		int damage = badguys.get(badguynum).getdmgs().get(atk); //variable for the attack damage
		int cost = badguys.get(badguynum).getcosts().get(atk); //Variable for the attack cost
		
		String type = badguys.get(badguynum).gettype(); //Variable for the badguy type
		String resistance = userpokes.get(chosenpoke).getresistance(); //Variable for goodguy resistance
		String weakness = userpokes.get(chosenpoke).getweakness(); //Variable for goodguy weakness
		String special = badguys.get(badguynum).getspecials().get(atk); //Variable for attack special
		
		if(special.equals("wild card")){ //If special is wild card
			if (wildcard()==true){ //Checks to see if it is true
				System.out.println("-------------------------------"); //Dashed line for clean look
				System.out.printf("%s's attack was not successful!\nIt's your turn.\n", badguys.get(badguynum).getname()); //The attack was not successful
				System.out.println("-------------------------------");
				EORenergy(); //End of round energy recharge check
				moveselect(); //Moves on to good guy attack
			}
		}
		
		if(special.equals("wild storm")){ //If the special is wild storm, run the wild storm attack method instead of this
			wildstorm(badguys.get(badguynum), userpokes.get(chosenpoke), badguys.get(badguynum).getatks().get(atk), damage, cost, type, resistance, weakness);
		}
		System.out.println("---------------------------------");//Dashed line for clean look
		System.out.printf("Enemy %s used %s.\n" , badguys.get(badguynum).getname() , badguys.get(badguynum).getatks().get(atk) ); //Prints badguy name and attack
		
		if(special.equals("recharge")){ //Checks if special is recharge
			System.out.printf("%s gained %d energy because of Recharge!\n", badguys.get(badguynum).getname(), rechargeAmnt(badguys.get(badguynum).getenergy()));
			badguys.get(badguynum).energyRecharge(rechargeAmnt(badguys.get(badguynum).getenergy()));
		}	//Uses object and rechargeAmnt to add energy
		
			
		if(resistancecheck(type,resistance)==true){ //Checks to see if the defending pokemon has a resistance
			System.out.println("The attack was not very effective ...");
			damage*=0.5; //If true, cuts the damage in half
		}
		if(weaknesscheck(type, weakness)==true){ //Checks to see if the defending pokemon has a weakness
			System.out.println("The attack was super effective!");
			damage*=2; //If true, doubles the damage
		}
		
		if(badguys.get(badguynum).getdisable()==true){ //Checks if the badguy is disabled
			damage-=10; //takes away 10 damage
			if(damage<0){ //Makes sure the damage does not go negative
				damage=0;
			}
		}
		
		stun(special, userpokes.get(chosenpoke)); //Checks to see if the badguy can stun
		
		disable(special, userpokes.get(chosenpoke)); //Checks to see if the badguy can disable
		
		System.out.printf("It did %d damage.\n" , damage); //How much damage the attack did
		
		badguys.get(badguynum).attackcost(cost); //Uses object to take away attack cost for badguy
		userpokes.get(chosenpoke).attackdmg(damage); //Uses object to deal damage to goodguy
		
		int hp = userpokes.get(chosenpoke).gethp(); //Variable for goodguy hp
		
		if(hp<0){ //makes sure hp does not go below 0
			hp = 0;
		}
		
		System.out.printf("%s has %d HP left!\n" , userpokes.get(chosenpoke).getname(), hp); //How much hp the good guy has left
		System.out.println("---------------------------------\n");
		if(userpokes.get(chosenpoke).gethp()==0 || userpokes.get(chosenpoke).gethp()<0){ //If the good guy is defeated
			goodguydefeat(); //Run goodguy defeat method
		}
		
		else{ //If it is not defeated
			System.out.println("-----------------");
			System.out.println("It is your turn!");
			System.out.println("-----------------\n");
			turn = 1; //Numerical value for turn is changed to good guy
			EORenergy(); //End of round energy check
			moveselect(); //Runs the good guy mvoe select method
		}
	}
	
	//-----------------------------------------------------------------------------------------------------------------	
}