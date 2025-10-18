package co.edu.karolsaavedra.unabshop

import android.util.Patterns

//retornar un true si es valido y un salfse si no es valido
//retorne tambien una cadena que diga si es valido o no


//iniciar sesión
fun validateEmail (email: String):Pair <Boolean, String>{ //pair, retornar los dos tipos de datos
    return when{
        email.isEmpty() -> Pair(false, "EL correo es requerido")
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->Pair(false, "El correo es invalido")
        email.endsWith("@test.com") -> Pair(false, "Ese email no es corporativo")  //verificar que el email termine en test.com, o en el que se desee
        else -> {
            Pair (true, "") //todo quedó bien
        }
    }

}

fun validatePassword(password:String): Pair<Boolean, String>{
    return when{
        password.isEmpty() -> Pair(false, "La contraseña es requerida.")
        password.length < 8 -> Pair(false, "La contraseña debe al menos tener 8 caracteres") //colocar un minimo de caracteres en la contraseña
        !password.any {it.isDigit()} -> Pair(false, "La conraseña debe tener al menos un número") //debe tener números
        else -> Pair(true, "")
    }
}

//registro
fun validateName (name :String): Pair <Boolean, String>{
    return when {
        name.isEmpty() -> Pair(false, "El nombre es requerido")
        name.length < 3 -> Pair(false, "El nombre debe tener al menos 3 caracteres")
        else -> Pair(true,"")
    }

}

fun validateConfirmPassword (password: String, confirmpassword: String): Pair <Boolean, String>{
    return  when{
        confirmpassword.isEmpty() -> Pair(false, "La contraseña es requerida.")
        confirmpassword != password -> Pair(false, "Las contraseñas no coinciden")
        else -> Pair(true, "")
    }

}
