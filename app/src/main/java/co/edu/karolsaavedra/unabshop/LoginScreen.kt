package co.edu.karolsaavedra.unabshop

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onClickRegister:()-> Unit = {}, onSuccesfulogin: () -> Unit ={}) {

    val auth = Firebase.auth
    val activity = LocalView.current.context as Activity

    //Estados
    var inputEmail by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("")}


    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)//hacer que el teclado no tape los campos que se van a llenar
                .fillMaxSize()
                .imePadding()//hacer que el teclado no tape los campos que se van a llenar
                .verticalScroll(rememberScrollState())//hacer que el teclado no tape los campos que se van a llenar,
                .padding(horizontal = 32.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            // Ícono de Usuario (Material Icons)
            Image(
                painter = painterResource(id = R.drawable.img_icon_unab),
                contentDescription = "Usuario",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Título
            Text(
                text = "Iniciar Sesión",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF9900)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo de Correo Electrónico
            OutlinedTextField(
                value = inputEmail, // Valor vacío (sin estado)
                onValueChange = {inputEmail = it},
                label = { Text("Correo Electrónico") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = Color(0xFF666666) // Color gris
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                //mostrar mensaje de error por si algún dato quedó mal digitado
                supportingText = {
                    if (emailError.isNotEmpty()){
                        Text(
                            text = emailError,
                            color = Color.Red
                        )
                    }
                }

                )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = inputPassword, // Valor vacío (sin estado)
                onValueChange = {inputPassword = it},
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Contraseña",
                        tint = Color(0xFF666666) // Color gris
                    )
                },
                // al momento de digitar la contraseña que no se vea en el campo
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false
                ),
                modifier = Modifier.fillMaxWidth(),
                //mostrar mensaje de error por si algún dato quedó mal digitado
                supportingText = {
                    if(passwordError.isNotEmpty()){
                        Text(
                            text = passwordError,
                            color = Color.Red
                        )
                    }
                },


                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6200EE), // Color morado
                    unfocusedBorderColor = Color(0xFFCCCCCC) // Color gris claro
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (loginError.isNotEmpty()){
                Text(
                    loginError,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }

            // Botón de Iniciar Sesión
            Button(
                onClick = {

                    //TODA LA PARTE DE VALIDACIONES SE CONECTA CON VALIDATIONS.KT
                    //agregar la validación de datos, y que pueda ingresar correctamente
                    val isValidEmail: Boolean = validateEmail(inputEmail).first //.first devuelve el valor booleano, si necesitaramos el string se colocaría .second
                    val isValidPassword = validatePassword(inputPassword).first

                    //variables por si ocurre algún error al ingresar los datos
                    emailError = validateEmail(inputEmail).second //.second va a devolver el String
                    passwordError = validatePassword(inputPassword).second //.second va a devolver el String

                    if (isValidEmail && isValidPassword){ //validar tanto el email como la contraseña
                        // colocar datos para poder iniciar sesión
                        auth.signInWithEmailAndPassword(inputEmail, inputPassword)
                            .addOnCompleteListener (activity){ task ->
                                if (task.isSuccessful){
                                    onSuccesfulogin()
                                }else{
                                    loginError= when(task.exception){ //tipo de advertencias  de error que van a aparecer si la contraseña o correo están mal, o si no existe el correo
                                        is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrecta"
                                        is FirebaseAuthInvalidUserException -> "No existe una ceunta con este correo"
                                        else -> "Error al iniciar sesión. Intenta de nuevo"
                                    }

                                }
                            }
                    }




                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9900)) // Color morado
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Enlace para Registrarse
            TextButton(onClick = onClickRegister ) {
                Text(
                    text = "¿No tienes una cuenta? Regístrate",
                    color = Color(0xFFFF9900)
                )
            }
        }
    }
}