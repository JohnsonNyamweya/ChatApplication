package com.nyamweyajohnson.chatapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.nyamweyajohnson.chatapplication.R
import com.nyamweyajohnson.chatapplication.model.Message

class MessageAdapter(private val context: Context, private val messageList: ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //This will help to return the view
    val ITEM_SENT = 2
    val ITEM_RECEIVE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){

            //inflate receive layout
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive_layout, parent, false)
            return ReceiveViewHolder(view)

        }
        else{

            //inflate sent layout
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent_layout, parent, false)
            return SentViewHolder(view)

        }
    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }
        else{
            return ITEM_RECEIVE
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java){
            //write code dealing with SentViewHolder

            val sentViewHolder = holder as SentViewHolder

            holder.txtSentMessage.text = currentMessage.message
        }
        else{
            //write code dealing with ReceiveViewHolder

            val receiveViewHolder = holder as ReceiveViewHolder

            holder.txtReceiveMessage.text = currentMessage.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val txtSentMessage = itemView.findViewById<TextView>(R.id.txtV_sent_message)
    }

    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val txtReceiveMessage = itemView.findViewById<TextView>(R.id.txtV_receive_message)
    }

}