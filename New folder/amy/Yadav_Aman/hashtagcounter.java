import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.util.*;
public class hashtagcounter {
	
		public static void main(String[] args) throws IOException {
			// TODO Auto-generated method stub
			
			//read inputs from file.
			BufferedReader br = null;
			BufferedWriter bw = null;
			FileWriter fw =null;
			try {
				Scanner in = new Scanner(System.in);				
				String getPath = args[0];
				
				FileInputStream fstream = new FileInputStream(getPath);
				DataInputStream inputBuffer = new DataInputStream(fstream);
				br = new BufferedReader(new InputStreamReader(inputBuffer));
				
				File OutputFile = new File("output_file.txt");
				if (!OutputFile.exists()) {
					OutputFile.createNewFile();
				}
				fw = new FileWriter(OutputFile);
				bw = new BufferedWriter(fw);
				
				String strLine;
				
				//regex to match input that contains just number.
				String regex2 = "(([0-9]+))";
				
				  //Read InputFile Line By Line
				  while ((strLine = br.readLine()) != null)   {
					  String[] inputs= strLine.split(" ");
					  if(inputs[0].indexOf("#")>=0)
					  {
						  String hashtag = inputs[0].replace("#", "");
						  FibonacciHeap.InsertOrIncreaseKey(hashtag,Integer.parseInt(inputs[1]));
					  }
					  else if(inputs[0].matches(regex2))
					  {
						  //System.out.println(inputs[0]);
						  int count =Integer.parseInt(inputs[0]);
						  ArrayList<FibonacciNode> outputList = FibonacciHeap.GetTopNHashTag(count);
						  int i=0;
						  //bw.write(outputList.get(0).hashtagValue + ',');
						  for(i = 0; i<outputList.size()-1 ; i++)
						  {
							  bw.write(outputList.get(i).hashtagValue + ",");
						     // System.out.println('{'+outputList.get(i).hashtagValue+ ',' +outputList.get(i).count+'}' +',');
						  }
						  bw.write(outputList.get(i).hashtagValue + "\n");
						  //System.out.println('{'+outputList.get(i).hashtagValue+ ',' +outputList.get(i).count+'}' +',');						  
					  }
					  else if(strLine.matches("STOP"))
					  {
						  System.out.println("Output File is generated");
						  break;
					  }				  
				  }
			}
			catch(Exception e)
	        {
				br.close();				
				bw.close();
				fw.close();
	            System.out.println("Error Occured");
	        }
			finally
			{
				br.close();			
				bw.close();
				fw.close();
			}
	}
	
}
