package com.nyamweyajohnson.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nyamweyajohnson.chatapplication.Adapter.MessageAdapter
import com.nyamweyajohnson.chatapplication.model.Message

class ChatActivity : AppCompatActivity() {

    private lateinit var chatsRecyclerView: RecyclerView
    private lateinit var edtMessage: EditText
    private lateinit var sendButton: ImageView

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    private lateinit var chatDbRef: DatabaseReference

    //variables for creating unique room for sender and receiver (private)
    private var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        chatDbRef = FirebaseDatabase.getInstance().reference

        senderRoom = receiverUid + senderUid

        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        chatsRecyclerView = findViewById(R.id.chats_recyclerview)
        edtMessage = findViewById(R.id.edt_message)
        sendButton = findViewById(R.id.img_send)

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        chatsRecyclerView.layoutManager = LinearLayoutManager(this)
        chatsRecyclerView.adapter = messageAdapter

        //add data to the recyclerview
        chatDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }

                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        sendButton.setOnClickListener{
            //send the message to the database and from the it, the message will be received to different user

            val message = edtMessage.text.toString()
            val messageObject = Message(message, senderUid)

            chatDbRef.child("chats").child(senderRoom!!).child("messages")
                    .push().setValue(messageObject).addOnSuccessListener {
                    chatDbRef.child("chats").child(receiverRoom!!).child("messages")
                        .push().setValue(messageObject)
                }
            edtMessage.setText("")

        }

    }
}