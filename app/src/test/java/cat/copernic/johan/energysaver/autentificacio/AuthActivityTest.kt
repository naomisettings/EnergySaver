package cat.copernic.johan.energysaver.autentificacio

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase
import org.junit.Test

class AuthActivityTest : TestCase() {
    private val auth = AuthActivity()
    private lateinit var successTask: Task<AuthResult>
    private lateinit var failureTask: Task<AuthResult>


    private lateinit var mAuth: FirebaseAuth


    private var logInResult = UNDEF

    companion object {
        private const val SUCCESS = 1
        private const val FAILURE = -1
        private const val UNDEF = 0
    }
    @Test
    public override fun setUp() {
        super.setUp()
    }
    @Test
    public override fun tearDown() {}
    @Test
    fun testOnStart() {}
    @Test
    fun testOnActivityResult() {}
    @Test
    fun testFirebaseAuthWithGoogle() {}
    @Test
    fun testSignIn() {
        val email = "angels@hotmail.com"
        val password = "123456"

        assert(logInResult == SUCCESS)
    }
    @Test
    fun testSignInWithGoogle() {}
    @Test
    fun testSignOUt() {}
    @Test
    fun testReload() {}
    @Test
    fun testValidateFormat() {}
    @Test
    fun testOnClick() {}
}