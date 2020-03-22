package com.example.mychatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_chat.*
import kotlinx.android.synthetic.main.user_row_new_chat.view.*

class NewChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)

        supportActionBar?.title = "Select User"

//        val adapter = GroupAdapter<GroupieViewHolder>()
//
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//
//        recycleView_newChat.adapter = adapter

        performFetchUser()
    }

    private fun performFetchUser(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                recycleView_newChat.adapter = adapter

                p0.children.forEach {task ->
                    Log.d("NewChatActivity", task.toString())
                    val user = task.getValue(User::class.java)
                    if(user != null) adapter.add(UserItem(user))
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}

class UserItem(val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_textView_userRow_newChat.text = user.username

        Picasso.get().load(user.profilePhotoURL).into(viewHolder.itemView.photoCircle_imageView_userRow_newChat)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_chat
    }
}
