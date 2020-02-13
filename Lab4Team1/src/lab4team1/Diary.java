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

   public void addDiary(String addition)
   {
      entry += (addition + "\r\n");
   }

   @Override
   public String toString()
   {
      return (this.entry);
   }

   public String getEntry()
   {
      return entry;
   }

   public void setEntry(String entry)
   {
      this.entry = entry;
   }

}
