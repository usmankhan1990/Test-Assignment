package fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.interviewtest.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import activities.MainActivity;
import co.sspp.library.SweetAlertDialog;
import view.UiView;

public class SignUpFragment extends Fragment {

    TextView txtTerms;
    Button btnSignUp;
    EditText edtFirstName,edtLastName,edtEmail,edtPassword,edtPhoneNumber;
    String firstName = "", lastName = "", email = "", password = "", phoneNumber = "";
    SweetAlertDialog pDialog;
    private UiView uiViewInstance = UiView.getInstance();
    SweetAlertDialog dialogError;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.sign_up_screen, container, false);
        txtTerms = rootView.findViewById(R.id.txtTerms);
        btnSignUp = rootView.findViewById(R.id.btnSignUp);


        edtFirstName    = rootView.findViewById(R.id.edtFirstName);
        edtLastName     = rootView.findViewById(R.id.edtLastName);
        edtEmail        = rootView.findViewById(R.id.edtEmail);
        edtPassword     = rootView.findViewById(R.id.edtPassword);
        edtPhoneNumber  = rootView.findViewById(R.id.edtPhoneNumber);

        dialogError = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);

        txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                startActivity(browserIntent);

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName   = edtFirstName.getText().toString();
                lastName    = edtLastName.getText().toString();
                email       = edtEmail.getText().toString();
                password    = edtPassword.getText().toString();
                phoneNumber = edtPhoneNumber.getText().toString();

                if(firstName==null || firstName.equalsIgnoreCase("")||lastName==null || lastName.equalsIgnoreCase("")
                        ||email==null || email.equalsIgnoreCase("")||password==null || password.equalsIgnoreCase("")
                        ||phoneNumber==null || phoneNumber.equalsIgnoreCase("")){
                    uiViewInstance.dialogBox(dialogError,"Kindly fill full information!");
                }else{
                    if(password.length()<6){
                        uiViewInstance.dialogBox(dialogError,"Minimum password length is 6 digits!");
                    }else{
                        pDialog = uiViewInstance.showProgressBar(getActivity());
                        signUp(firstName, lastName, email, phoneNumber, password);
                    }
                }




            }
        });

        return rootView;
    }



    public void signUp(String firstName, String lastName, String email, String phoneNumber, final String password){


        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("password", password);

        pDialog =  uiViewInstance.showProgressBar(getActivity());

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {

                if (e == null) {
                    pDialog.dismiss();
                    try {
                        Toast.makeText(getActivity(),"Sign up Successful",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }catch (Exception exp){
                        exp.getMessage();
                    }


                } else {
                    pDialog.dismiss();
                    if(e.getMessage().equalsIgnoreCase("Account already exists for this username.")){
                        Toast.makeText(getActivity(),"Account already exists for this username.", Toast.LENGTH_LONG).show();
                    }else{

                    }

                }
            }
        });

    }

}