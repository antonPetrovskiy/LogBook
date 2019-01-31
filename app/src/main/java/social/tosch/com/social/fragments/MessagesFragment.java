package social.tosch.com.social.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import social.tosch.com.social.Controller;
import social.tosch.com.social.R;
import social.tosch.com.social.activities.DialogActivity;
import social.tosch.com.social.activities.LoginActivity;
import social.tosch.com.social.activities.Main2Activity;
import social.tosch.com.social.adapter.ContactAdapter;
import social.tosch.com.social.adapter.DialogAdapter;
import social.tosch.com.social.adapter.DialogFeedAdapter;
import social.tosch.com.social.api.ApiService;
import social.tosch.com.social.api.RetroClient;
import social.tosch.com.social.entity.Contact;
import social.tosch.com.social.entity.Dialog;
import social.tosch.com.social.entity.DialogList;
import social.tosch.com.social.entity.Message;

import static android.view.View.GONE;

public class MessagesFragment extends Fragment {
    static Context context;
    private static ListView listView;
    public static ArrayList<Dialog> dialogList;
    public static ArrayList<Dialog> dialogListNotSorted;
    private static DialogAdapter adapter;
    public boolean isNews = false;
    public boolean isChat = false;
    private ConstraintLayout lay;

    private static ListView listViewNews;
    public static ArrayList<String> newsList;
    private static DialogFeedAdapter adapterNews;
    private static ArrayList<String> messageList;
    private static ArrayList<String> messageTextList;
    private ImageView menu;
    private ImageView newChat;
    private ImageView bot;
    private ImageView line;
    private Animation show;
    private Animation hide;
    private Animation show_list;
    View rootView;


    public ImageView move;

    public MessagesFragment() {
        // Required empty public constructor
    }

    public static MessagesFragment newInstance(Context c) {
        MessagesFragment fragment = new MessagesFragment();
        context = c;


        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        init();
        buttons();
        update();
        updateNews();

        return rootView;
    }

    public void init(){
        //profile = rootView.findViewById(R.id.imageView35);
        menu = rootView.findViewById(R.id.imageView15);
        bot = rootView.findViewById(R.id.test1);
        newChat = rootView.findViewById(R.id.imageView16);
        move = rootView.findViewById(R.id.imageView24);
        line = rootView.findViewById(R.id.line);
        lay = rootView.findViewById(R.id.constraintLayout);
        show = AnimationUtils.loadAnimation(context, R.anim.show_bot);
        hide = AnimationUtils.loadAnimation(context, R.anim.hide_bot);
        show_list = AnimationUtils.loadAnimation(context, R.anim.show_bot_list);

        dialogList = new ArrayList<>();
        dialogListNotSorted = new ArrayList<>();
        listView = rootView.findViewById(R.id.listMessages);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DialogActivity.class);
                intent.putExtra("id", dialogListNotSorted.get(position).getUser_id());
                startActivity(intent);
            }
        });


        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(lay);
            constraintSet.connect(R.id.listMessages,ConstraintSet.BOTTOM,R.id.test1, ConstraintSet.BOTTOM);
        constraintSet.applyTo(lay);
        newsList = new ArrayList<>();
        newsList.add("else");
        newsList.add("sell_buy");
        newsList.add("service");
        newsList.add("sport");
        listViewNews = rootView.findViewById(R.id.listGroups);
        listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DialogActivity.class);
                intent.putExtra("id", newsList.get(position));
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteDialog(i);
                return true;
            }
        });

        messageList = new ArrayList<>();
        messageTextList = new ArrayList<>();
        move.setVisibility(GONE);
        listViewNews.setVisibility(GONE);
        line.setVisibility(GONE);

    }

    public void buttons(){
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main2Activity.drawer.openDrawer(Gravity.START);
            }
        });

        bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move.setVisibility(View.VISIBLE);
                listViewNews.setVisibility(View.VISIBLE);
                move.startAnimation(show);
                listViewNews.startAnimation(show);
                isNews = true;
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(lay);
                constraintSet.connect(R.id.listMessages,ConstraintSet.BOTTOM,R.id.listGroups, ConstraintSet.TOP,-30);
                constraintSet.applyTo(lay);
                line.startAnimation(show_list);
                bot.setVisibility(GONE);
                line.setVisibility(View.VISIBLE);
                listView.setBackgroundResource(R.drawable.news_bg_half);
            }
        });

        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                move.setVisibility(GONE);
                listViewNews.setVisibility(GONE);
                listViewNews.startAnimation(hide);
                isNews = false;
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(lay);
                constraintSet.connect(R.id.listMessages,ConstraintSet.BOTTOM,R.id.test1, ConstraintSet.BOTTOM);
                constraintSet.applyTo(lay);
                bot.setVisibility(View.VISIBLE);
                line.setVisibility(GONE);
                listView.setBackgroundResource(R.drawable.news_bg);

            }
        });



        newChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.layout_find_friends, null);
                final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();
                final ListView listView = promptView.findViewById(R.id.list);
                final ArrayList<Contact> tmp = new ArrayList<>();
                final ImageView btn = promptView.findViewById(R.id.imageView25);
                btn.setVisibility(GONE);
                EditText editText = promptView.findViewById(R.id.editText);
                ImageView exit = promptView.findViewById(R.id.imageView9);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertD.hide();
                    }
                });
                editText.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tmp.clear();
                        for(int j = 0; j < Controller.getInstance(context).getContactList().size(); j ++){
                            if(Controller.getInstance(context).getContactList().get(j).getName().toLowerCase().contains(s.toString().toLowerCase())){
                                tmp.add(Controller.getInstance(context).getContactList().get(j));
                            }
                        }
                        ContactAdapter adapter = new ContactAdapter(context, tmp);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }

                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(context, DialogActivity.class);
                        if(tmp.size()==0){
                            intent.putExtra("id", Controller.getInstance(context).getContactList().get(i).getId());
                        }else{
                            intent.putExtra("id", tmp.get(i).getId());
                        }
                        startActivity(intent);
                    }
                });
                ContactAdapter adapter;
                adapter = new ContactAdapter(context, Controller.getInstance(context).getContactList());
                listView.setAdapter(adapter);
                listView.setSelectionAfterHeaderView();
                alertD.setView(promptView);
                alertD.show();
            }
        });
    }

    public static void update(){

        if (((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            ApiService api = RetroClient.getApiService();
            Call<DialogList> call = api.getDialogList("Bearer " + LoginActivity.stringBuilder.toString());
            call.enqueue(new Callback<DialogList>() {
                @Override
                public void onResponse(Call<DialogList> call, Response<DialogList> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getIds()!=null) {
                            dialogList = new ArrayList<>();
                            dialogList = response.body().getIds();
                            dialogListNotSorted = response.body().getIds();
                            Collections.sort(dialogList, Dialog.COMPARE_BY_LASTMESSAGE);
                            Collections.reverse(dialogList);
                            adapter = new DialogAdapter(context, dialogList);
                            if(listView!=null)
                                listView.setAdapter(adapter);



                        }
                    } else {
                        Log.i("LISTERF" , "no success");
                    }
                }

                @Override
                public void onFailure(Call<DialogList> call, Throwable t) {
                    Log.i("LISTERF" , t.getMessage()+" ");
                }
            });

        } else {
            Log.i("LIST" , "no internet");
        }

    }

    public void updateNews(){
        if (newsList.size() != 0) {
            adapterNews = new DialogFeedAdapter(context, newsList, messageTextList);
            listViewNews.setAdapter(adapterNews);
        }
    }

    public void deleteDialog(int n){
        final int i = n;
        String title = "Удалить диалог?";
        String button1String = "Да";
        String button2String = "Нет";
        AlertDialog.Builder ad;
        ad = new AlertDialog.Builder(getActivity());
        ad.setTitle(title);  // заголовок
        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if (((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                    ApiService api = RetroClient.getApiService();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_id", dialogList.get(i).getUser_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonParser jsonParser = new JsonParser();
                    JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
                    Call<ResponseBody> call = api.deleteDialog("Bearer " + LoginActivity.stringBuilder.toString(),dialogList.get(i).getUser_id());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Log.i("REMOVEDIALOG" , "SUCCESS");
                                update();
                            } else {
                                JSONObject jObjError = null;
                                try {
                                    jObjError = new JSONObject(response.errorBody().string());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Log.i("REMOVEDIALOG" , jObjError.getString("message")+dialogList.get(i).getUser_id()+" ");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                } else {
                    Log.i("LIST" , "no internet");
                }
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        ad.show();

    }

    public void bot(){
        bot.performClick();
    }
}
