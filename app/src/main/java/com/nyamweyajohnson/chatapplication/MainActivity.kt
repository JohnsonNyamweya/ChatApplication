package com.nyamweyajohnson.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nyamweyajohnson.chatapplication.Adapter.UserAdapter
import com.nyamweyajohnson.chatapplication.model.User

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView

    private lateinit var mAuth: FirebaseAuth

    //This recyclerView need two things
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter

    //database reference
    private lateinit var userDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = ("Chat")

        mAuth = FirebaseAuth.getInstance()
        userDatabaseReference = FirebaseDatabase.getInstance().reference

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        userRecyclerView = findViewById(R.id.user_recycleView)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        //get inside database and read the values
        userDatabaseReference.child("User").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear previous userList
                userList.clear()

                for (postSnapshot in snapshot.children){

                    val currentUser = postSnapshot.getValue(User::class.java)

                    if (mAuth.currentUser?.uid != currentUser?.uId){
                        userList.add(currentUser!!)
                    }

                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            //write logic for logout
            mAuth.signOut()

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()

            return true
        }
        return true
    }

}