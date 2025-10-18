package co.edu.karolsaavedra.unabshop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.auth
import com.google.firebase.ktx.Firebase

@Composable
fun NavegationApp(){

    val myNavController = rememberNavController()
    var myStartDestination: String = "login"

    val auth = com.google.firebase.Firebase.auth
    val currntUser = auth.currentUser

    if(currntUser != null){
        myStartDestination = "home"
    }else{
        myStartDestination = "login"
    }

    NavHost( //poder pasar de una pestaña a otra
        navController = myNavController,
        startDestination = myStartDestination
    ){
        composable ("login") {
            LoginScreen(onClickRegister = {
                myNavController.navigate("register")
            }, onSuccesfulogin = {//si se le da atras se sale de la aplicación
                myNavController.navigate("home"){
                    popUpTo("login"){inclusive = true}
                }
            }
            )
        }
        composable("register") {
            RegisterScreen(
                onClickBack = {
                    myNavController.popBackStack()
                },
                onSuccessfulRegister = {
                    myNavController.navigate("home") {
                        popUpTo(0)
                    }
                }
            )
        }
        composable("home"){
            HomeScreen(onClickLogout = {
                myNavController.navigate("login"){
                    popUpTo(0)
                }
            })
    }
    }
}