package fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.interviewtest.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import activities.MainActivity;
import co.sspp.library.SweetAlertDialog;
import view.UiView;

public class LoginFragment extends Fragment {

    TextView txtTerms;
    Button btnLogin;
    SweetAlertDialog pDialog;
    private UiView uiViewInstance = UiView.getInstance();
    EditText edtEmail,edtPassword;
    String email = "", password = "";
    SweetAlertDialog dialogError;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.login_screen, container, false);
        txtTerms    = rootView.findViewById(R.id.txtTerms);
        btnLogin    = rootView.findViewById(R.id.btnLogin);
        edtEmail    = rootView.findViewById(R.id.edtEmail);
        edtPassword = rootView.findViewById(R.id.edtPassword);

        dialogError = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);

        txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                startActivity(browserIntent);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email    = edtEmail.getText().toString();
                password = edtPassword.getText().toString();

                if(email==null||password==null||email.equalsIgnoreCase("")||password.equalsIgnoreCase("")){
                        uiViewInstance.dialogBox(dialogError,"Kindly fill full information!");
                }else{

                    if(password.length()<6){
                        uiViewInstance.dialogBox(dialogError,"Minimum password length is 6 digits!");
                    }else{
                        pDialog = uiViewInstance.showProgressBar(getActivity());
                        ParseUser.logInInBackground(email, password, new LogInCallback() {

                            @Override
                            public void done(ParseUser user, ParseException e) {

                                pDialog.dismiss();
                                if (user != null) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    if(e.getMessage().equalsIgnoreCase("Invalid username/password.")){
                                        uiViewInstance.dialogBox(dialogError,"Incorrect email or password");
                                    }else{
                                        uiViewInstance.dialogBox(dialogError,"Please try again later!");
                                    }
                                }
                            }
                        });
                    }
                }


            }
        });




        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return rootView;
    }






}