package br.com.fadergs.myexpenses;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AjustaPerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajusta_perfil);

        // Instancia do use logado
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Colocando o nome no campo nome
        EditText edtnome = findViewById(R.id.perfil_nome);
        edtnome.setText(firebaseUser.getDisplayName());

        //Colocando o email no campo email
        TextView txtEmail = findViewById(R.id.perfil_email);
        txtEmail.setText(firebaseUser.getEmail());


        //Botão Altera nome
        Button btnSalvar = findViewById(R.id.perfil_btnSalvaAlteracao);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavUtils.navigateUpFromSameTask(AjustaPerfilActivity.this);
                trocaNomeFireBaseAuth(firebaseUser);

            }
        });

        //Botão troca senha
        Button btnTrocaSenha = findViewById(R.id.perfil_btnTrocaSenha);
        btnTrocaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abre a caixa de dialogo com a confirmação para a troca de senha.
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                enviaEmailTrocaSenha(firebaseUser.getEmail());
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AjustaPerfilActivity.this);
                builder.setMessage("Um email será enviado para a conta: "+ firebaseUser.getEmail()+"\n Deseja continuar?").setPositiveButton(R.string.sim, dialogClickListener)
                        .setNegativeButton(R.string.nao, dialogClickListener).show();
            }
        });

        //Botão para Excluir a conta

        Button btnExcluirConta = findViewById(R.id.perfil_btnExcluiConta);
        btnExcluirConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abre a caixa de dialogo com a confirmação para a troca de senha.
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                //Yes button clicked
                                excluiContaFireBase();
                                excluiDadosFirebaseDB();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AjustaPerfilActivity.this);
                builder.setMessage(R.string.notificacao_exclusao).setPositiveButton(R.string.sim,dialogClickListener)
                        .setNegativeButton(R.string.nao, dialogClickListener).show();

            }
        });



    }


    // metodo para excluir todos os dados do usuario no Firebase DB
    private  void excluiDadosFirebaseDB(){
        Toast.makeText(this, "Ainda não foi implementado!", Toast.LENGTH_LONG).show();
    }

    //metodo para excluir a conta no Firebase Atuh
    private void excluiContaFireBase(){
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Deletion succeeded
                            Toast.makeText(AjustaPerfilActivity.this, "Conta Excluída com sucesso!!!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Deletion failed
                            Toast.makeText(AjustaPerfilActivity.this, "ERRO AO EXCLUIR CONTA!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Envia email para a troca de senha
    private void enviaEmailTrocaSenha(String emailAddress){

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AjustaPerfilActivity.this, "Email enviado para: "+ auth.getCurrentUser().getEmail().toString(), Toast.LENGTH_LONG).show();

                        }else {
                            Toast.makeText(AjustaPerfilActivity.this, "ERRO AO ENVIAR EMAIL PARA: "+ auth.getCurrentUser().getEmail().toString(), Toast.LENGTH_LONG);
                        }
                    }
                });
    }

    //Troca o nome no FirebaseAuth
    private void trocaNomeFireBaseAuth(FirebaseUser user){

        EditText edtNome = findViewById(R.id.perfil_nome);

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(edtNome.getText().toString())
                .build();

        // Atualiza o nome do user
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AjustaPerfilActivity.this, "Nome alterado com sucesso!", Toast.LENGTH_LONG).show();
                            startActivity(getIntent());
                        }
                    }
                });
    }
}
