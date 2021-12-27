package com.company.untitled.screen.blank;


import io.jmix.ui.component.Button;
import io.jmix.ui.component.TextField;
import io.jmix.ui.component.ValuesPicker;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

@UiController("BlankScreen")
@UiDescriptor("blank-screen.xml")
public class BlankScreen extends Screen {

    @Autowired
    private ValuesPicker arratFirst;
    @Autowired
    private ValuesPicker arraySecond;
    @Autowired
    private TextField resultText;
    @Autowired
    private TextField<Integer> cube9;
    @Autowired
    private TextField<Integer> cube8;
    @Autowired
    private TextField<Integer> cube7;
    @Autowired
    private TextField<Integer> cube6;
    @Autowired
    private TextField<Integer> cube5;
    @Autowired
    private TextField<Integer> cube4;
    @Autowired
    private TextField<Integer> cube3;
    @Autowired
    private TextField<Integer> cube2;
    @Autowired
    private TextField<Integer> cube1;
    @Autowired
    private TextField res9;
    @Autowired
    private TextField res8;
    @Autowired
    private TextField res7;
    @Autowired
    private TextField res6;
    @Autowired
    private TextField res5;
    @Autowired
    private TextField res4;
    @Autowired
    private TextField res3;
    @Autowired
    private TextField res2;
    @Autowired
    private TextField res1;
    @Autowired
    private TextField costField;

    public String getToken() {
        try {
            Process process = Runtime.getRuntime().exec("curl -X POST http://localhost:8080/oauth/token --basic --user client:secret " +
                    "-H \"Content-Type: application/x-www-form-urlencoded\" -d \"grant_type=password&username=admin&password=admin\"");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();
            System.out.println(response);
            JSONObject objectToken = new JSONObject(response.toString());
            String access_token = objectToken.getString("access_token");
            return access_token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Subscribe("computeArr")
    public void onComputeArrClick(Button.ClickEvent event) {
        String token = getToken();
        String arrF = Arrays.stream(arratFirst.getValue().toString().substring(1, arratFirst.getValue().toString().length() - 1).split(", ")).collect(Collectors.joining(","));
        String arrS = Arrays.stream(arraySecond.getValue().toString().substring(1, arraySecond.getValue().toString().length() - 1).split(", ")).collect(Collectors.joining(","));

        try {
                URL url = new URL("http://localhost:8080/rest/services/array_rest_service/computeArr?arrayFirst="+arrF+"&arraySecond="+arrS);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuffer responseResult = new StringBuffer();
                String output;
                while ((output = in.readLine()) != null) {
                    responseResult.append(output);
                }
                in.close();
                resultText.setValue(responseResult.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    @Subscribe("saveArr")
    public void onSaveArrClick(Button.ClickEvent event) {
        String token = getToken();
        String arr = arratFirst.getValue().toString().substring(1,arratFirst.getValue().toString().length()-1);
        try {
            URL url = new URL("http://localhost:8080/rest/services/rest_testrt/saveArr?arrayFirst="+arr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe("computeSq")
    public void onComputeSqClick(Button.ClickEvent event) {
        String token = getToken();
        URL url = null;
        try {
            url = new URL("http://localhost:8080/rest/services/square_rest_service/computeSquare?cube1="+cube1.getValue()+"&cube2="+
                    cube2.getValue()+"&cube3="+cube3.getValue()+"&cube4="+cube4.getValue()+"&cube5="+cube5.getValue()+"&cube6="+cube6.getValue()+"&cube7="+cube7.getValue()+"&cube8="+
                    cube8.getValue()+"&cube9="+cube9.getValue());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String sq;
            StringBuffer cubePre = new StringBuffer();
            while ((sq=bufferedReader.readLine())!=null){
                cubePre.append(sq);
            }
            bufferedReader.close();
            JSONArray cube = new JSONArray(cubePre.toString());

            costField.setValue("Cost: "+cube.get(0));
            res1.setValue(cube.get(1));
            res2.setValue(cube.get(2));
            res3.setValue(cube.get(3));
            res4.setValue(cube.get(4));
            res5.setValue(cube.get(5));
            res6.setValue(cube.get(6));
            res7.setValue(cube.get(7));
            res8.setValue(cube.get(8));
            res9.setValue(cube.get(9));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Subscribe("saveSquare")
    public void onSaveSquareClick(Button.ClickEvent event) {
        String token = getToken();
        try {
            URL url = new URL("http://localhost:8080/rest/services/square_rest_service/saveSquare?cube1=" + cube1.getValue() + "&cube2=" +
                    cube2.getValue() + "&cube3=" + cube3.getValue() + "&cube4=" + cube4.getValue() + "&cube5=" + cube5.getValue() + "&cube6=" + cube6.getValue() + "&cube7=" + cube7.getValue() + "&cube8=" +
                    cube8.getValue() + "&cube9=" + cube9.getValue());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}