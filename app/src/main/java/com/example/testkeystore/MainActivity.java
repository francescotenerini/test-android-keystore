package com.example.testkeystore;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;


import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyInfo;
import android.security.keystore.KeyProperties;

import java.security.InvalidAlgorithmParameterException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.testkeystore.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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
                String antani = "";
                try {
                    KeyGenerator keyGenerator = KeyGenerator.getInstance(
                            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

                    // Set the key alias (a unique name for the key)
                    String keyAlias = "MySecretKey";

                    // Create a KeyGenParameterSpec for AES
                    KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                            keyAlias,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC) // Or other block mode
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7) // Or other padding
                            // .setIsStrongBoxBacked(true)
                            .build();

                    keyGenerator.init(keyGenParameterSpec);
                    SecretKey key = keyGenerator.generateKey();

                    SecretKeyFactory factory = SecretKeyFactory.getInstance(key.getAlgorithm(), "AndroidKeyStore");
                    KeyInfo keyInfo;
                    try {
                        keyInfo = (KeyInfo) factory.getKeySpec(key, KeyInfo.class);
                        switch (keyInfo.getSecurityLevel()) {
                            case KeyProperties.SECURITY_LEVEL_SOFTWARE:
                                Log.w("testkeystore", "+++ SECURITY_LEVEL_SOFTWARE");
                                antani = "SECURITY_LEVEL_SOFTWARE";
                                break;
                            case KeyProperties.SECURITY_LEVEL_STRONGBOX:
                                Log.w("testkeystore", "+++ SECURITY_LEVEL_STRONGBOX");
                                antani = "SECURITY_LEVEL_STRONGBOX";
                                break;
                            case KeyProperties.SECURITY_LEVEL_TRUSTED_ENVIRONMENT:
                                Log.w("testkeystore", "+++ SECURITY_LEVEL_TRUSTED_ENVIRONMENT");
                                antani = "SECURITY_LEVEL_TRUSTED_ENVIRONMENT";
                                break;
                            case KeyProperties.SECURITY_LEVEL_UNKNOWN:
                                Log.w("testkeystore", "+++ SECURITY_LEVEL_UNKNOWN");
                                antani = "SECURITY_LEVEL_UNKNOWN";
                                break;
                            case KeyProperties.SECURITY_LEVEL_UNKNOWN_SECURE:
                                Log.w("testkeystore", "+++ SECURITY_LEVEL_UNKNOWN_SECURE");
                                antani = "SECURITY_LEVEL_UNKNOWN_SECURE";
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

                Snackbar.make(view, "Security level " + antani, Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
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