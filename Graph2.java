import java.util.*;
import java.io.*;


public class Graph2
{
    public class Vertex
    {
        HashMap<String, Integer> nbrs = new HashMap<>();
    }

    static HashMap<String, Vertex> vtces;

    public Graph2()
    {
        vtces = new HashMap<>();
    }

    public int numVetex()
    {
        return this.vtces.size();
    }

    public boolean containsVertex(String vname)
    {
        return this.vtces.containsKey(vname);
    }

    public void addVertex(String vname)
    {
        Vertex vtx = new Vertex();
        vtces.put(vname, vtx);
    }

    public void removeVertex(String vname)
    {
        Vertex vtx = vtces.get(vname);
        ArrayList<String> keys = new ArrayList<>(vtx.nbrs.keySet());

        for (String key : keys)
        {
            Vertex nbrVtx = vtces.get(key);
            nbrVtx.nbrs.remove(vname);
        }

        vtces.remove(vname);
    }

    public int numEdges()
    {
        ArrayList<String> keys = new ArrayList<>(vtces.keySet());
        int count = 0;

        for (String key : keys)
        {
            Vertex vtx = vtces.get(key);
            count = count + vtx.nbrs.size();
        }

        return count / 2;
    }

    public boolean containsEdge(String vname1, String vname2)
    {
        Vertex vtx1 = vtces.get(vname1);
        Vertex vtx2 = vtces.get(vname2);

        if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
            return false;
        }

        return true;
    }

    public void addEdge(String vname1, String vname2, int value)
    {
        Vertex vtx1 = vtces.get(vname1);
        Vertex vtx2 = vtces.get(vname2);

        if (vtx1 == null || vtx2 == null || vtx1.nbrs.containsKey(vname2)) {
            return;
        }

        vtx1.nbrs.put(vname2, value);
        vtx2.nbrs.put(vname1, value);
    }

    public void removeEdge(String vname1, String vname2)
    {
        Vertex vtx1 = vtces.get(vname1);
        Vertex vtx2 = vtces.get(vname2);

        //check if the vertices given or the edge between these vertices exist or not
        if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
            return;
        }

        vtx1.nbrs.remove(vname2);
        vtx2.nbrs.remove(vname1);
    }

    public void display_Map()
    {
        System.out.println("\t Hyderabad Metro Map");
        System.out.println("\t------------------");
        System.out.println("----------------------------------------------------\n");
        ArrayList<String> keys = new ArrayList<>(vtces.keySet());

        for (String key : keys)
        {
            String str = key + " =>\n";
            Vertex vtx = vtces.get(key);
            ArrayList<String> vtxnbrs = new ArrayList<>(vtx.nbrs.keySet());

            for (String nbr : vtxnbrs)
            {
                str = str + "\t" + nbr + "\t";
                if (nbr.length()<16)
                    str = str + "\t";
                if (nbr.length()<8)
                    str = str + "\t";
                str = str + vtx.nbrs.get(nbr) + "\n";
            }
            System.out.println(str);
        }
        System.out.println("\t------------------");
        System.out.println("---------------------------------------------------\n");

    }

    public void display_Stations()
    {
        System.out.println("\n***********************************************************************\n");
        ArrayList<String> keys = new ArrayList<>(vtces.keySet());
        int i=1;
        for(String key : keys)
        {
            System.out.println(i + ". " + key);
            i++;
        }
        System.out.println("\n***********************************************************************\n");
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean hasPath(String vname1, String vname2, HashMap<String, Boolean> processed)
    {
        // DIR EDGE
        if (containsEdge(vname1, vname2)) {
            return true;
        }

        //MARK AS DONE
        processed.put(vname1, true);

        Vertex vtx = vtces.get(vname1);
        ArrayList<String> nbrs = new ArrayList<>(vtx.nbrs.keySet());

        //TRAVERSE THE NBRS OF THE VERTEX
        for (String nbr : nbrs)
        {

            if (!processed.containsKey(nbr))
                if (hasPath(nbr, vname2, processed))
                    return true;
        }

        return false;
    }


    private class DijkstraPair implements Comparable<DijkstraPair>
    {
        String vname;
        String psf;
        int cost;

        @Override
        public int compareTo(DijkstraPair o)
        {
            return o.cost - this.cost;
        }
    }

    public int dijkstra(String src, String des, boolean nan)
    {
        int val = 0;
        ArrayList<String> ans = new ArrayList<>();
        HashMap<String, DijkstraPair> map = new HashMap<>();

        Heap<DijkstraPair> heap = new Heap<>();

        for (String key : vtces.keySet())
        {
            DijkstraPair np = new DijkstraPair();
            np.vname = key;
            //np.psf = "";
            np.cost = Integer.MAX_VALUE;

            if (key.equals(src))
            {
                np.cost = 0;
                np.psf = key;
            }

            heap.add(np);
            map.put(key, np);
        }

        //keep removing the pairs while heap is not empty
        while (!heap.isEmpty())
        {
            DijkstraPair rp = heap.remove();

            if(rp.vname.equals(des))
            {
                val = rp.cost;
                break;
            }

            map.remove(rp.vname);

            ans.add(rp.vname);

            Vertex v = vtces.get(rp.vname);
            for (String nbr : v.nbrs.keySet())
            {
                if (map.containsKey(nbr))
                {
                    int oc = map.get(nbr).cost;
                    Vertex k = vtces.get(rp.vname);
                    int nc;
                    if(nan)
                        nc = rp.cost + 120 + 40*k.nbrs.get(nbr);
                    else
                        nc = rp.cost + k.nbrs.get(nbr);

                    if (nc < oc)
                    {
                        DijkstraPair gp = map.get(nbr);
                        gp.psf = rp.psf + nbr;
                        gp.cost = nc;

                        heap.updatePriority(gp);
                    }
                }
            }
        }
        return val;
    }

    private class Pair
    {
        String vname;
        String psf;
        int min_dis;
        int min_time;
    }

    public String Get_Minimum_Distance(String src, String dst)
    {
        int min = Integer.MAX_VALUE;
        //int time = 0;
        String ans = "";
        HashMap<String, Boolean> processed = new HashMap<>();
        LinkedList<Pair> stack = new LinkedList<>();

        // create a new pair
        Pair sp = new Pair();
        sp.vname = src;
        sp.psf = src + "  ";
        sp.min_dis = 0;
        sp.min_time = 0;

        // put the new pair in stack
        stack.addFirst(sp);

        // while stack is not empty keep on doing the work
        while (!stack.isEmpty())
        {
            // remove a pair from stack
            Pair rp = stack.removeFirst();

            if (processed.containsKey(rp.vname))
            {
                continue;
            }

            // processed put
            processed.put(rp.vname, true);

            //if there exists a direct edge b/w removed pair and destination vertex
            if (rp.vname.equals(dst))
            {
                int temp = rp.min_dis;
                if(temp<min) {
                    ans = rp.psf;
                    min = temp;
                }
                continue;
            }

            Vertex rpvtx = vtces.get(rp.vname);
            ArrayList<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());

            for(String nbr : nbrs)
            {
                // process only unprocessed nbrs
                if (!processed.containsKey(nbr)) {

                    // make a new pair of nbr and put in queue
                    Pair np = new Pair();
                    np.vname = nbr;
                    np.psf = rp.psf + nbr + "  ";
                    np.min_dis = rp.min_dis + rpvtx.nbrs.get(nbr);
                    //np.min_time = rp.min_time + 120 + 40*rpvtx.nbrs.get(nbr);
                    stack.addFirst(np);
                }
            }
        }
        ans = ans + Integer.toString(min);
        return ans;
    }


    public String Get_Minimum_Time(String src, String dst)
    {
        int min = Integer.MAX_VALUE;
        String ans = "";
        HashMap<String, Boolean> processed = new HashMap<>();
        LinkedList<Pair> stack = new LinkedList<>();

        // create a new pair
        Pair sp = new Pair();
        sp.vname = src;
        sp.psf = src + "  ";
        sp.min_dis = 0;
        sp.min_time = 0;

        // put the new pair in queue
        stack.addFirst(sp);

        // while queue is not empty keep on doing the work
        while (!stack.isEmpty()) {

            // remove a pair from queue
            Pair rp = stack.removeFirst();

            if (processed.containsKey(rp.vname))
            {
                continue;
            }

            // processed put
            processed.put(rp.vname, true);

            //if there exists a direct edge b/w removed pair and destination vertex
            if (rp.vname.equals(dst))
            {
                int temp = rp.min_time;
                if(temp<min) {
                    ans = rp.psf;
                    min = temp;
                }
                continue;
            }

            Vertex rpvtx = vtces.get(rp.vname);
            ArrayList<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());

            for (String nbr : nbrs)
            {
                // process only unprocessed nbrs
                if (!processed.containsKey(nbr)) {

                    // make a new pair of nbr and put in queue
                    Pair np = new Pair();
                    np.vname = nbr;
                    np.psf = rp.psf + nbr + "  ";
                    //np.min_dis = rp.min_dis + rpvtx.nbrs.get(nbr);
                    np.min_time = rp.min_time + 120 + 40*rpvtx.nbrs.get(nbr);
                    stack.addFirst(np);
                }
            }
        }
        Double minutes = Math.ceil((double)min / 60);
        ans = ans + Double.toString(minutes);
        return ans;
    }

    public ArrayList<String> get_Interchanges(String str)
    {
        ArrayList<String> arr = new ArrayList<>();
        String res[] = str.split("  ");
        arr.add(res[0]);
        int count = 0;
        for(int i=1;i<res.length-1;i++)
        {
            int index = res[i].indexOf('~');
            String s = res[i].substring(index+1);

            if(s.length()==2)
            {
                String prev = res[i-1].substring(res[i-1].indexOf('~')+1);
                String next = res[i+1].substring(res[i+1].indexOf('~')+1);

                if(prev.equals(next))
                {
                    arr.add(res[i]);
                }
                else
                {
                    arr.add(res[i]+" ==> "+res[i+1]);
                    i++;
                    count++;
                }
            }
            else
            {
                arr.add(res[i]);
            }
        }
        arr.add(Integer.toString(count));
        arr.add(res[res.length-1]);
        return arr;
    }

    public static void Create_Metro_Map(Graph2 g)
    {
        g.addVertex("Miyapur~R");
        g.addVertex("Kukatpally~R");
        g.addVertex("Bharat Nagar~R");
        g.addVertex("Ameerpet~RB");
        g.addVertex("Khairatabad~R");
        g.addVertex("Nampally~R");
        g.addVertex("MG Bus Station~RG");
        g.addVertex("Dilsukhnagar~R");
        g.addVertex("LB Nagar~R");
        g.addVertex("Nagole~B");
        g.addVertex("Uppal~B");
        g.addVertex("Secunderabad East~B");
        g.addVertex("Parade Ground~BG");
        g.addVertex("Begumpet~B");
        g.addVertex("Jubilee Hills Check Post~B");
        g.addVertex("Madhapur~B");
        g.addVertex("Raidurg~B");
        g.addVertex("Gandhi Hospital~G");
        g.addVertex("RTC X Roads~G");
        g.addVertex("Sultan Bazaar~G");


        g.addEdge("Miyapur~R", "Kukatpally~R", 5);
        g.addEdge("Kukatpally~R", "Bharat Nagar~R", 3);
        g.addEdge("Bharat Nagar~R", "Ameerpet~RB", 4);
        g.addEdge("Ameerpet~RB", "Khairatabad~R", 3);
        g.addEdge("Khairatabad~R", "Nampally~R", 3);
        g.addEdge("Nampally~R", "MG Bus Station~RG", 2);
        g.addEdge("MG Bus Station~RG", "Dilsukhnagar~R", 5);
        g.addEdge("Dilsukhnagar~R", "LB Nagar~R", 4);
        g.addEdge("Nagole~B", "Uppal~B", 1);
        g.addEdge("Uppal~B", "Secunderabad East~B", 8);
        g.addEdge("Secunderabad East~B", "Parade Ground~BG", 2);
        g.addEdge("Parade Ground~BG", "Begumpet~B", 5);
        g.addEdge("Begumpet~B", "Ameerpet~RB", 2);
        g.addEdge("Ameerpet~RB", "Jubilee Hills Check Post~B", 4);
        g.addEdge("Jubilee Hills Check Post~B", "Madhapur~B", 2);
        g.addEdge("Madhapur~B", "Raidurg~B", 4);
        g.addEdge("Parade Ground~BG", "Gandhi Hospital~G", 3);
        g.addEdge("Gandhi Hospital~G", "RTC X Roads~G", 2);
        g.addEdge("RTC X Roads~G", "Sultan Bazaar~G", 3);
        g.addEdge("Sultan Bazaar~G", "MG Bus Station~RG", 1);


    }

    public static String[] printCodelist()
    {
        System.out.println("List of station along with their codes:\n");
        ArrayList<String> keys = new ArrayList<>(vtces.keySet());
        int i=1,j=0,m=1;
        StringTokenizer stname;
        String temp="";
        String codes[] = new String[keys.size()];
        char c;
        for(String key : keys)
        {
            stname = new StringTokenizer(key);
            codes[i-1] = "";
            j=0;
            while (stname.hasMoreTokens())
            {
                temp = stname.nextToken();
                c = temp.charAt(0);
                while (c>47 && c<58)
                {
                    codes[i-1]+= c;
                    j++;
                    c = temp.charAt(j);
                }
                if ((c<48 || c>57) && c<123)
                    codes[i-1]+= c;
            }
            if (codes[i-1].length() < 2)
                codes[i-1]+= Character.toUpperCase(temp.charAt(1));

            System.out.print(i + ". " + key + "\t");
            if (key.length()<(22-m))
                System.out.print("\t");
            if (key.length()<(14-m))
                System.out.print("\t");
            if (key.length()<(6-m))
                System.out.print("\t");
            System.out.println(codes[i-1]);
            i++;
            if (i == (int)Math.pow(10,m))
                m++;
        }
        return codes;
    }

    public static void main(String[] args) throws IOException
    {
        Graph2 g = new Graph2();
        Create_Metro_Map(g);

        System.out.println("\n\t\t\t****WELCOME TO THE METRO APP*****");
        BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));

        while(true)
        {
            System.out.println("\t\t\t\t~~LIST OF ACTIONS~~\n\n");
            System.out.println("1. LIST ALL THE STATIONS IN THE MAP");
            System.out.println("2. SHOW THE METRO MAP");
            System.out.println("3. GET SHORTEST DISTANCE FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
            System.out.println("4. GET SHORTEST TIME TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
            System.out.println("5. GET SHORTEST PATH (DISTANCE WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
            System.out.println("6. GET SHORTEST PATH (TIME WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
            System.out.println("7. EXIT THE MENU");
            System.out.print("\nENTER YOUR CHOICE FROM THE ABOVE LIST (1 to 7) : ");
            int choice = -1;
            try {
                choice = Integer.parseInt(inp.readLine());
            } catch(Exception e) {
                // default will handle
            }
            System.out.print("\n***********************************************************\n");
            if(choice == 7)
            {
                System.exit(0);
            }
            switch(choice)
            {
                case 1:
                    g.display_Stations();
                    break;

                case 2:
                    g.display_Map();
                    break;

                case 3:
                    ArrayList<String> keys = new ArrayList<>(vtces.keySet());
                    String codes[] = printCodelist();
                    System.out.println("\n1. TO ENTER SERIAL NO. OF STATIONS\n2. TO ENTER CODE OF STATIONS\n3. TO ENTER NAME OF STATIONS\n");
                    System.out.println("ENTER YOUR CHOICE:");
                    int ch = Integer.parseInt(inp.readLine());
                    int j;

                    String st1 = "", st2 = "";
                    System.out.println("ENTER THE SOURCE AND DESTINATION STATIONS");
                    if (ch == 1)
                    {
                        st1 = keys.get(Integer.parseInt(inp.readLine())-1);
                        st2 = keys.get(Integer.parseInt(inp.readLine())-1);
                    }
                    else if (ch == 2)
                    {
                        String a,b;
                        a = (inp.readLine()).toUpperCase();
                        for (j=0;j<keys.size();j++)
                            if (a.equals(codes[j]))
                                break;
                        st1 = keys.get(j);
                        b = (inp.readLine()).toUpperCase();
                        for (j=0;j<keys.size();j++)
                            if (b.equals(codes[j]))
                                break;
                        st2 = keys.get(j);
                    }
                    else if (ch == 3)
                    {
                        st1 = inp.readLine();
                        st2 = inp.readLine();
                    }
                    else
                    {
                        System.out.println("Invalid choice");
                        System.exit(0);
                    }

                    HashMap<String, Boolean> processed = new HashMap<>();
                    if(!g.containsVertex(st1) || !g.containsVertex(st2) || !g.hasPath(st1, st2, processed))
                        System.out.println("THE INPUTS ARE INVALID");
                    else
                        System.out.println("SHORTEST DISTANCE FROM "+st1+" TO "+st2+" IS "+g.dijkstra(st1, st2, false)+"KM\n");
                    break;

                case 4:
                    ArrayList<String> keys2 = new ArrayList<>(vtces.keySet());
                    String codes2[] = printCodelist();
                    System.out.println("\n1. TO ENTER SERIAL NO. OF STATIONS\n2. TO ENTER CODE OF STATIONS\n3. TO ENTER NAME OF STATIONS\n");
                    System.out.println("ENTER YOUR CHOICE:");
                     ch = Integer.parseInt(inp.readLine());


                    String sat1 = "", sat2 = "";
                    System.out.println("ENTER THE SOURCE AND DESTINATION STATIONS");
                    if (ch == 1)
                    {
                        sat1 = keys2.get(Integer.parseInt(inp.readLine())-1);
                        sat2 = keys2.get(Integer.parseInt(inp.readLine())-1);
                    }
                    else if (ch == 2)
                    {
                        String a,b;
                        a = (inp.readLine()).toUpperCase();
                        for (j=0;j<keys2.size();j++)
                            if (a.equals(codes2[j]))
                                break;
                        sat1 = keys2.get(j);
                        b = (inp.readLine()).toUpperCase();
                        for (j=0;j<keys2.size();j++)
                            if (b.equals(codes2[j]))
                                break;
                        sat2 = keys2.get(j);
                    }
                    else if (ch == 3)
                    {
                        sat1 = inp.readLine();
                        sat2 = inp.readLine();
                    }
                    else
                    {
                        System.out.println("Invalid choice");
                        System.exit(0);
                    }


                    HashMap<String, Boolean> processed1= new HashMap<>();
                    System.out.println("SHORTEST TIME FROM ("+sat1+") TO ("+sat2+") IS "+g.dijkstra(sat1, sat2, true)/60+" MINUTES\n\n");
                    break;

                case 5:
                    ArrayList<String> keys3 = new ArrayList<>(vtces.keySet());
                    String codes3[] = printCodelist();
                    System.out.println("\n1. TO ENTER SERIAL NO. OF STATIONS\n2. TO ENTER CODE OF STATIONS\n3. TO ENTER NAME OF STATIONS\n");
                    System.out.println("ENTER YOUR CHOICE:");
                     ch = Integer.parseInt(inp.readLine());


                    String s1 = "", s2 = "";
                    System.out.println("ENTER THE SOURCE AND DESTINATION STATIONS");
                    if (ch == 1)
                    {
                        s1 = keys3.get(Integer.parseInt(inp.readLine())-1);
                        s2 = keys3.get(Integer.parseInt(inp.readLine())-1);
                    }
                    else if (ch == 2)
                    {
                        String a,b;
                        a = (inp.readLine()).toUpperCase();
                        for (j=0;j<keys3.size();j++)
                            if (a.equals(codes3[j]))
                                break;
                        s1 = keys3.get(j);
                        b = (inp.readLine()).toUpperCase();
                        for (j=0;j<keys3.size();j++)
                            if (b.equals(codes3[j]))
                                break;
                        s2 = keys3.get(j);
                    }
                    else if (ch == 3)
                    {
                        s1 = inp.readLine();
                        s2 = inp.readLine();
                    }
                    else
                    {
                        System.out.println("Invalid choice");
                        System.exit(0);
                    }

                    HashMap<String, Boolean> processed2 = new HashMap<>();
                    if(!g.containsVertex(s1) || !g.containsVertex(s2) || !g.hasPath(s1, s2, processed2))
                        System.out.println("THE INPUTS ARE INVALID");
                    else
                    {
                        ArrayList<String> str = g.get_Interchanges(g.Get_Minimum_Distance(s1, s2));
                        int len = str.size();
                        System.out.println("SOURCE STATION : " + s1);
                        System.out.println("SOURCE STATION : " + s2);
                        System.out.println("DISTANCE : " + str.get(len-1));
                        System.out.println("NUMBER OF INTERCHANGES : " + str.get(len-2));
                        //System.out.println(str);
                        System.out.println("~~~~~~~~~~~~~");
                        System.out.println("START  ==>  " + str.get(0));
                        for(int i=1; i<len-3; i++)
                        {
                            System.out.println(str.get(i));
                        }
                        System.out.print(str.get(len-3) + "   ==>    END");
                        System.out.println("\n~~~~~~~~~~~~~");
                    }
                    break;

                case 6:
                    ArrayList<String> keys4 = new ArrayList<>(vtces.keySet());
                    String codes4[] = printCodelist();
                    System.out.println("\n1. TO ENTER SERIAL NO. OF STATIONS\n2. TO ENTER CODE OF STATIONS\n3. TO ENTER NAME OF STATIONS\n");
                    System.out.println("ENTER YOUR CHOICE:");
                     ch = Integer.parseInt(inp.readLine());


                    String ss1 = "", ss2 = "";
                    System.out.println("ENTER THE SOURCE AND DESTINATION STATIONS");
                    if (ch == 1)
                    {
                        ss1 = keys4.get(Integer.parseInt(inp.readLine())-1);
                        ss2 = keys4.get(Integer.parseInt(inp.readLine())-1);
                    }
                    else if (ch == 2)
                    {
                        String a,b;
                        a = (inp.readLine()).toUpperCase();
                        for (j=0;j<keys4.size();j++)
                            if (a.equals(codes4[j]))
                                break;
                        ss1 = keys4.get(j);
                        b = (inp.readLine()).toUpperCase();
                        for (j=0;j<keys4.size();j++)
                            if (b.equals(codes4[j]))
                                break;
                        ss2 = keys4.get(j);
                    }
                    else if (ch == 3)
                    {
                        ss1 = inp.readLine();
                        ss2 = inp.readLine();
                    }
                    else
                    {
                        System.out.println("Invalid choice");
                        System.exit(0);
                    }

                    HashMap<String, Boolean> processed3 = new HashMap<>();
                    if(!g.containsVertex(ss1) || !g.containsVertex(ss2) || !g.hasPath(ss1, ss2, processed3))
                        System.out.println("THE INPUTS ARE INVALID");
                    else
                    {
                        ArrayList<String> str = g.get_Interchanges(g.Get_Minimum_Time(ss1, ss2));
                        int len = str.size();
                        System.out.println("SOURCE STATION : " + ss1);
                        System.out.println("DESTINATION STATION : " + ss2);
                        System.out.println("TIME : " + str.get(len-1)+" MINUTES");
                        System.out.println("NUMBER OF INTERCHANGES : " + str.get(len-2));
                        //System.out.println(str);
                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        System.out.print("START  ==>  " + str.get(0) + " ==>  ");
                        for(int i=1; i<len-3; i++)
                        {
                            System.out.println(str.get(i));
                        }
                        System.out.print(str.get(len-3) + "   ==>    END");
                        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    }
                    break;
                default:
                    System.out.println("Please enter a valid option! ");
                    System.out.println("The options you can choose are from 1 to 6. ");

            }
        }
    }
}