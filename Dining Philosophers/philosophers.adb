with Ada.Numerics.Float_Random;  use Ada.Numerics.Float_Random;
with Ada.Text_IO;                use Ada.Text_IO;

procedure philosophers is
   Repeats : constant := 3;
   n : constant := 7;

   protected Printer is
      procedure Print(Id : Integer; S : String);
   end Printer;

   protected body Printer is
      procedure Print(Id : Integer; S : String) is
      begin
         Put_Line("Philosopher" & Integer'Image (Id+1) & S);
      end Print;
    end Printer;

   protected type Fork is
      entry Take;
      procedure PutDown;
   private
      Taken : Boolean := False;
   end Fork;
   
   protected Host is
      entry Enter;
      procedure Leave;
   private
      Guests : Integer := 0;
   end Host;

   protected body Fork is
      entry Take when not Taken is
      begin
         Taken := True;
      end Take;
      procedure PutDown is
      begin
         Taken := False;
      end PutDown;
   end Fork;

   protected body Host is
      entry Enter when Guests < n-1 is
      begin
         Guests := Guests + 1;
      end Enter;
      procedure Leave is
      begin
         Guests := Guests - 1;
      end Leave;
   end Host;
   
   task type Philosopher (ID : Integer; Left, Right : not null access Fork);
   task body Philosopher is
      Dice : Generator;
   begin
      Reset (Dice);
      for i in 1..Repeats loop
         Printer.Print(Id," is thinking");
         --Put_Line ("Philosopher" & Integer'Image (ID+1) & " is thinking");
         delay Duration (Random (Dice) + 1.0);
         --Put_Line ("Philosopher" & Integer'Image (ID+1) & " is hungry");
         Printer.Print(Id," is hungry");
         Host.Enter;
         Left.Take;
         Right.Take;
         --Put_Line ("Philosopher" & Integer'Image (ID+1) & " is eating");
         Printer.Print(Id," is eating");
         delay Duration (Random (Dice) + 1.0);
         Left.PutDown;
         Right.PutDown;
         Host.Leave;
      end loop;
      Printer.Print(Id," finished");
      --Put_Line ("Philosopher" & Integer'Image (ID) & " finished");
   end Philosopher;

   type Guest is access Philosopher;

   Forks : array (0..n-1) of aliased Fork;
   Guests : Guest;
begin
   for i in 0..n-1 loop
      Guests := new Philosopher(i, Forks (i)'Access, Forks ((i+1) mod n)'Access);
   end loop;
end philosophers;