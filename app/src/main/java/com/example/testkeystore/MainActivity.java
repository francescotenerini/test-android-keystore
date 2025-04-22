package com.example.testkeystore;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;


import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyInfo;
import android.security.keystore.KeyProperties;

import java.security.InvalidAlgorithmParameterException;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.testkeystore.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String securityLevelString = "";
                try {
                    // Set the key alias (a unique name for the key)
                    String keyAlias = "MySecretKey";

                    // Generate RSA keypair
                    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                            KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
                    keyPairGenerator.initialize(
                            new KeyGenParameterSpec.Builder(
                                    keyAlias,
                                    KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                                    .build());
                    KeyPair keyPair = keyPairGenerator.generateKeyPair();
                    PrivateKey key = keyPair.getPrivate();

                    // Check the security level of the key
                    KeyFactory factory = KeyFactory.getInstance(key.getAlgorithm(), "AndroidKeyStore");
                    KeyInfo keyInfo;
                    try {
                        keyInfo = factory.getKeySpec(key, KeyInfo.class);
                        switch (keyInfo.getSecurityLevel()) {
                            case KeyProperties.SECURITY_LEVEL_SOFTWARE:
                                Log.w("testkeystore", "+++ SECURITY_LEVEL_SOFTWARE");
                                securityLevelString = "SECURITY_LEVEL_SOFTWARE";
                                break;
                            case KeyProperties.SECURITY_LEVEL_STRONGBOX:
                                Log.w("testkeystore", "+++ SECURITY_LEVEL_STRONGBOX");
                                securityLevelString = "SECURITY_LEVEL_STRONGBOX";
                                break;
                            case KeyProperties.SECURITY_LEVEL_TRUSTED_ENVIRONMENT:
                                Log.w("testkeystore", "+++ SECURITY_LEVEL_TRUSTED_ENVIRONMENT");
                                securityLevelString = "SECURITY_LEVEL_TRUSTED_ENVIRONMENT";
                                break;
                            case KeyProperties.SECURITY_LEVEL_UNKNOWN:
                                Log.w("testkeystore", "+++ SECURITY_LEVEL_UNKNOWN");
                                securityLevelString = "SECURITY_LEVEL_UNKNOWN";
                                break;
                            case KeyProperties.SECURITY_LEVEL_UNKNOWN_SECURE:
                                Log.w("testkeystore", "+++ SECURITY_LEVEL_UNKNOWN_SECURE");
                                securityLevelString = "SECURITY_LEVEL_UNKNOWN_SECURE";
                                break;
                        }
                    } catch (InvalidKeySpecException e) {
                        throw new RuntimeException(e);
                    }
                } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                    throw new RuntimeException(e);
                } catch (InvalidAlgorithmParameterException e) {
                    throw new RuntimeException(e);
                }

                Snackbar.make(view, "Security level " + securityLevelString, Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();

                // To generate a CSR:
                // https://stackoverflow.com/questions/37850134/what-is-the-certificate-enrollment-process
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}