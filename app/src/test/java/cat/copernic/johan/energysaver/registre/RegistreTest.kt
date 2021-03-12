package cat.copernic.johan.energysaver.registre

import android.icu.util.TimeUnit
import android.service.autofill.Validators.not
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreSettings
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.core.Is
import org.hamcrest.core.StringContains
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.util.concurrent.ExecutionException



class RegistreTest : TestCase() {

    companion object {
        private const val COLLECTION_USER = "usuaris"
    }


    @Rule
    @JvmField
    val expectedException = ExpectedException.none()

    fun expectFirestorePermissionDenied() {
        expectedException.expect(ExecutionException::class.java)
        expectedException.expectCause(isA(FirebaseFirestoreException::class.java))
        expectedException.expectMessage(StringContains("PERMISSION_DENIED"))
    }
    private var crearUsuari = Registre()
   // private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    @Before
    public override fun setUp() {
       var db = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        db.firestoreSettings = settings

        auth = FirebaseAuth.getInstance()

         var user1 = testCreateAccount()
    }
    @After
    public override fun tearDown() {}
    @Test
    fun testRecollirUsuari() {}

    @Test
    fun testCreateAccount() {

    }









}