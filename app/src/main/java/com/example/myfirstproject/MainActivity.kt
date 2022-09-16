package com.example.myfirstproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstproject.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var adapter = QuoteListAdapter()
    var databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 내용, 출처 저장
        binding.mainBtnInsert.setOnClickListener{
            val content = binding.mainEtContentEdt.text.toString()
            val from = binding.mainEtFromEdt.text.toString()

            // firebase에 저장
            saveQuote(content, from)
        }

        // 어댑터 연결
        binding.rvQuote.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvQuote.adapter = adapter

        // 데이터 불러오기
        loadQuoteList()


    }


    fun saveQuote(content: String, from : String){
        if(content.isEmpty()){
            Toast.makeText(this, "명언을 작성하세요", Toast.LENGTH_SHORT).show()
            return
        }
        else if(from.isEmpty()){
            Toast.makeText(this, "출처를 작성하세요", Toast.LENGTH_SHORT).show()
            return
        }

        // 데이터베이스이 /quotes/자식개체를 만들고 키값을 리턴
        val key: String? = databaseRef.child("/quotes").push().key

        // (id, content, from)로 Quote 데이터 클래스 생성
        val quote=Quote(key!!, content, from)

        // 데이터를 Map 개체로 변환
        val quoteValues: HashMap<String, Any> = quote.toMap()

        // 수정이 가능한 MutableMap를 만들어 데이터를 저장
        val childUpdates : MutableMap<String, Any> = HashMap()
        childUpdates["/quotes/$key"] = quoteValues

        // Firebase Database에 반영
        databaseRef.updateChildren(childUpdates)
        Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_SHORT).show()
    }

    fun loadQuoteList() {
        databaseRef.child("/quotes").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                println("${error.toException()}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.quoteList.clear()
                val collectionIterator = snapshot.children.iterator()

                if(collectionIterator.hasNext()){
                    while (collectionIterator.hasNext()){
                        val currentItem = collectionIterator.next()
                        val map = currentItem.getValue() as HashMap<String, Any>

                        val id = map.get("id") as String
                        val content = map.get("content") as String
                        val from = map.get("from") as String
                        adapter.quoteList.add(Quote(id, content, from))
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    fun removeQuote(quoteId: String){
        databaseRef.child("/quotes/${quoteId}").removeValue()
    }

}