package tw.com.flag.youtube_test_project;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by AllenLin on 2016/7/15.
 */
public class Filters {
    private ArrayList<String> person;
    private ArrayList<String> community;
    private double filterValue;
    private int KEY_WORD_COUNT = 3;

    public Filters(String person, String community, double filterValue) {
        super();
        this.person = String_dismantling(person);
        this.community = String_dismantling(community);
        this.filterValue = filterValue;
    }

    private ArrayList<String[]> Individual_weights(){
        ArrayList<String[]> results = new ArrayList<>();
        for(int i=0;i<person.size();i++){

            String[] input = new String[4];
            input[0] = person.get(i);
            input[1] = String.valueOf(filterValue * (KEY_WORD_COUNT - i));
            input[2] = input[1];
            input[3] = "0.0";
            results.add(input);
        }
        return results;
    }

    private ArrayList<String[]> Community_weights(){
        ArrayList<String[]> results = new ArrayList<>();
        for(int i=0;i<community.size();i++){

            String[] input = new String[4];
            input[0] = community.get(i);
            input[1] = String.valueOf((1 - filterValue) * (KEY_WORD_COUNT - i));
            input[2] = "0.0";
            input[3] = input[1];
            results.add(input);
        }
        return results;
    }

    private ArrayList<String> String_dismantling(String string){
        ArrayList<String> array = new ArrayList<String>();
        String[] res = string.trim().split(" ");
        for(int i=0;i<res.length;i++){
            if (res[i] != "") {
                array.add(res[i]);
            }
        }
        return array;
    }

    public ArrayList<String[]> Keyword_Combinations(){
        ArrayList<String[]> sort = new ArrayList<String[]>();
        sort.addAll(Individual_weights());
        sort.addAll(Community_weights());
        Sort_Weight(sort);
        ArrayList<String[]> combination = new ArrayList<String[]>();

        for (int i = 1; i < sort.size()+1; i++){
            combination = Parse_Combination(sort, combination, i, 0, null);
        }

        return Sort_Weight(combination);
    }

    private ArrayList<String[]> Parse_Combination(ArrayList<String[]> array
            , ArrayList<String[]> output, int length,int length_pos
            , ArrayList<Integer> length_ang) {
        if (length_pos == 0){
            length_pos = 1;
        }
        if (length_ang == null){
            length_ang = new ArrayList<Integer>();
        }

        if (length_ang.size() < length_pos)
        {
            if (length_pos == 1)
            {
                length_ang.add(1);
            }
            else
            {
                int prev_index = length_ang.get(length_ang.size()-1);
                length_ang.add(prev_index + 1);
            }
        }

        int index = 0;
        if (length_pos > 1)
        {
            int prev_index = length_ang.get(length_pos - 2);
            index = prev_index + 1;
        }

        if (length_ang.size() < length_pos)
        {
            length_ang.add(index);
        }

        for (int i = index; i < array.size() - (length - length_pos); i++)
        {
            if (length_ang.size() > length_pos)
            {
                ArrayList<Integer> temp = new ArrayList<Integer>();
                for (int p = 0; p < length_pos; p++){
                    temp.add(length_ang.get(p));
                }
                length_ang = temp;
            }

            length_ang.set(length_pos-1, i);

            if (length_ang.size() == length)
            {
                String[] output_ang = new String[4];
                output_ang[0] = "";
                output_ang[1] = "0.0";
                output_ang[2] = "0.0";
                output_ang[3] = "0.0";
                for (int a = 0 ; a < length_ang.size(); a++)
                {
                    index = length_ang.get(a);
                    String[] code = array.get(index);
                    output_ang[0] = output_ang[0] + " " + code[0];
                    Double weight = Double.valueOf(output_ang[1]) + Double.valueOf(code[1]);
                    output_ang[1] = weight.toString();
                    Double weight2 = Double.valueOf(output_ang[2]) + Double.valueOf(code[2]);
                    output_ang[2] = weight2.toString();
                    Double weight3 = Double.valueOf(output_ang[3]) + Double.valueOf(code[3]);
                    output_ang[3] = weight3.toString();
                }
                output.add(output_ang);
            }
            else
            {
                output = Parse_Combination(array, output, length, (length_pos + 1), length_ang);
            }
        }

        return output;
    }

    private ArrayList<String[]> Sort_Weight(ArrayList<String[]> list) {
        Collections.sort(list,
                new Comparator<String[]>() {
                    @Override
                    public int compare(String[] arg0, String[] arg1) {
                        // TODO Auto-generated method stub
                        return arg1[1].compareTo(arg0[1]);
                    }
                });

            for (int i = 0; i < list.size(); i++) {
                Log.d("TAG", list.get(i)[0]);
                Log.d("TAG", list.get(i)[1]);
                Log.d("TAG", list.get(i)[2]);
                Log.d("TAG", list.get(i)[3]);
            }

        return list;
    }

}
