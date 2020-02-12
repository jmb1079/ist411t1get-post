/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab4team1;


public class Diary
{
private String entry = "Begin\n-----\n";

   public Diary()
   {
   }

   public String getDiary()
   {
      return entry;
   }

   public void setDiary(String Entry)
   {
      this.entry = Entry;
   }
   
   public void addDiary(String addition){
      entry += addition;
      entry += "\r\n";
   }
   
@Override
   public String toString(){
      return(this.entry);
   }
   
}
