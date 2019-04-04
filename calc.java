/*
    William K. Holley, IV
    March 26th, 2018
    Data Structures Assignment # 4
    
    Purpose: This program accepts am expression from the user, converts it into a postfix, and then evaluates the expression
    Data Structures: Stack, ArrayList, Array
    The calc class contains the main method, along with controlling the entire program from user input to displaying the solved expression
*/

import java.util.*;


public class calc 
{
   public static void main(String args[])
   {
       // Stack and ArrayList Creation
       Stack<Character> charstack = new Stack<Character>();
       ArrayList<Character> charlist = new ArrayList<Character>();
      
       //Boolean Parameters for conversion  
       boolean lastOperator = false;
       boolean lastOpenParen = false;
       boolean lastClosedParen = false;
       boolean lastInt = false;
       boolean prevToken = false; // if prev token was an operand
      
       // Scanner for user input
       Scanner myscan = new Scanner(System.in);
       
       // User infix input
       System.out.println("Enter infix expression (q to quit): ");
       String infixString = myscan.nextLine();
       char infix[] = infixString.toCharArray();
      
       // Operator Between Operands Error
       if(infix[infix.length-1] == '/' || infix[infix.length-1] == '*' || infix[infix.length-1] == '%' ||
           infix[infix.length-1] == '+' || infix[infix.length-1] == '-'){
           System.out.println("Error in expression! No operator between operands. Also last token must be an operand.");
           System.exit(-1);
       }
      
       // No Operands or Open Parenthesis Before Operator Error
       if(infix[0] == '/' || infix[0] == '*' || infix[0] == '%' || infix[0] == '+' || infix[0] == '-'){
           System.out.println("Error in expression! First token must be an"
                   + " operand or open paranthesis.");
           System.exit(-1);
       }
      
       //Spacing Out Infix and Creating Tokens For Stack
       for(int i = 0; i < infixString.length(); i++)
        {
           if(infix[i] == ' ')
            {
               continue;
            }
           infix[i] = infixString.charAt(i);
           
           switch(infix[i])
           {
          
           // INTEGERS
           case '1': case '2': case '3': case '4':
           case '5': case '6': case '7': case '8':
           case '9': case '0':
               if(lastClosedParen){
                   System.out.println("Error in expression! An integer cannot directly"
                           + " follow a closed parenthesis.");
                   System.exit(-1);
               }
               
              if(lastInt)
              {
                   System.out.println("Error in expression! An integer cannot directly"
                           + " follow another integer.");
                   System.exit(-1);
              }
               lastOperator = false;
               lastOpenParen = false;
               lastClosedParen = false;
               lastInt = true;
               charlist.add(infix[i]);
               break;
              
           // SPACES
           case ' ': continue;
              
           // X User Input
           case 'x':try{
                       System.out.println("Enter value of x: ");
                       int xVarInt = myscan.nextInt();
                       String xVar = Integer.toString(xVarInt);
                       int c = xVar.length();
                       int a = 0;
                       while(a < c)
                               {
                                   charlist.add(xVar.charAt(a));
                                   a++;
                               }
                       }
           catch(InputMismatchException e)
           {
                           System.out.println("Error in exception! The x variable must be an integer!");
                           System.exit(-1);
                       }
                   break;
                  
           // UNARY OPERATORS & BINARY OPERATORS
           case '+': case '/':
                      if (lastOperator){
                          System.out.println("Error in expression! An operator cannot"
                               + " directly follow another operator.");
                          System.exit(-1);
                      }
                      if (lastOpenParen){
                          System.out.println("Error in expression! An operator cannot"
                               + " directly follow an open parenthesis.");
                          System.exit(-1);
                      }
                      lastOperator = true;
                      lastOpenParen = false;
                      lastClosedParen = false;
                      lastInt = false;
              
                      while(!charstack.isEmpty() && charstack.peek() != '(' &&
                              precedence(infix[i]) <= precedence(charstack.peek()))
                      {
                          charlist.add(charstack.pop());
                      }
                      charstack.push(infix[i]);
                      break;

           case '*':  
                              if (lastOpenParen){
                                  System.out.println("Error in expression! An operator cannot"
                                       + " directly follow an open parenthesis.");
                                  System.exit(-1);
                              }
                              lastOperator = true;
                              lastOpenParen = false;
                              lastClosedParen = false;
                              lastInt = false;
                           try{
                           while(!charstack.isEmpty() && charstack.peek() != '(' &&
                                      precedence(infix[i]) <= precedence(charstack.peek())){
                                  charlist.add(charstack.pop());
                              }
                              charstack.push('*');
                              break;
                           } catch(EmptyStackException e){
                                  System.out.println("Error in expresion! The * operator cannot be preceded by a * operator.");
                                  System.exit(-1);
                              }
                              while(charstack.peek() != '*')
                              {
                                  charlist.add('*');
                              }
                              charstack.push('*');
                              break;
                          
                     
           case '-': while(!charstack.isEmpty() && charstack.peek() != '(' &&
                      precedence(infix[i]) <= precedence(charstack.peek())){
                      charlist.add(charstack.pop());
                      }
                      charstack.push('-');
                      break;
                  
           // PARENTHESIS
           case '(': charstack.push(infix[i]);
                      lastOperator = false;
                      lastOpenParen = true;
                      lastClosedParen = false;
                      lastInt = false;
                      break;
                  
           case ')': if(lastOperator){
                       System.out.println("Error in expression! A closed parenthesis"
                       + " cannot directly follow an operator.");
                      System.exit(-1);
           }
                      lastOperator = false;
                      lastOpenParen = false;
                      lastClosedParen = true;
                      lastInt = false;
                      try{
                          while(charstack.peek() != '('){
                              charlist.add(charstack.pop());
                          }
                          charstack.pop();
                          break;
                      } catch(EmptyStackException e){
                          System.out.println("Error in expression! No matching left parentheses for"
                               + " a right parentheses.");
                          System.exit(-1);
                      }
          
           // Floating Error
           case '.': System.out.println("Error in expression! Cannot "
                   + "accept floating point numbers.");
                      System.exit(-1);
          
           // Quit
           case 'q': case 'Q':
                   System.out.println("Shutting down . . .");
                   System.out.println("Goodbye!");
                   System.exit(-1);
                  
                          
           // Illegal Character
           default:
               System.out.println("Illegal character used. Please try again."); break;
          
           } 
       } 
      
       while(!charstack.isEmpty()){
           if(charstack.peek() == '('){
               System.out.println("Error in expression! No matching right parenthesis for"
                       + " left parentehesis");
               System.exit(-1);
           }
           charlist.add(charstack.pop());
       }
       System.out.print("Converted Expression: ");
       for(int i = 0; i < charlist.size(); i++){
           System.out.print(charlist.get(i));
       }
       System.out.println();
       System.out.println("Answer to Expression: " + postFixEvaluation(charlist));
      
   }
   
  
   // PRECONDITION: Takes in a char variable to check for precedence
   // POSTCONDITION: Returns 2 for higher precedence and Returns 1 if anything else
   public static int precedence(char a){
       if(a == '/' || a == '%' || a == '*'){
           return 2;
       } else
           return 1;
   }
  
   // PRECONDITION: Takes in a postfix stored in a character arraylist
   // POSTCONDITION: Returns the answer to tyhe expression

   public static int postFixEvaluation(ArrayList<Character> a){
       Stack <Integer> evaluateStack = new Stack <Integer> ();
       for(int i = 0; i < a.size(); i++){
           if(Character.isDigit(a.get(i))){
               evaluateStack.push(Character.getNumericValue(a.get(i)));
           } else{
               int second = evaluateStack.pop();
               int first = evaluateStack.pop();
               switch(a.get(i)){
               case '+': evaluateStack.push(first + second); break;
               case '-': evaluateStack.push(first - second); break;
               case '*': evaluateStack.push(first * second); break;
               case '/': evaluateStack.push(first / second); break;
               case '%': evaluateStack.push(first % second); break;
               }
           }
       }
       return evaluateStack.pop();
   }
  
} 
