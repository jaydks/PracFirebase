package com.example.myfirstproject

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstproject.databinding.ItemQuoteBinding

class QuoteListAdapter : RecyclerView.Adapter<QuoteListAdapter.CustomViewHolder>() {

    var quoteList = ArrayList<Quote>()

    class CustomViewHolder(private val binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(quote: Quote) = with(binding){
            binding.itemQuoteTvContent.text = quote.content
            binding.itemQuoteTvFrom.text = quote.from
            binding.itemQuoteId.text = quote.id

            // 삭제
            binding.itemQuoteBtnDelete.setOnClickListener {
                MainActivity().removeQuote(binding.itemQuoteId.text.toString())
            }
            // 공유
            binding.itemQuoteBtnShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TITLE, "오늘의 명언")
                intent.putExtra(Intent.EXTRA_SUBJECT, "오늘의 명언")
                intent.putExtra(Intent.EXTRA_TEXT, "${quote.content}\n출처:${quote.from}")
                intent.type="text/plain"

                val chooser = Intent.createChooser(intent, "명언 공유")
                it.context.startActivity(chooser)
            }
            // 검색
            binding.itemQuoteBtnSearch.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${quote.from}"))
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuoteListAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
        return CustomViewHolder(ItemQuoteBinding.bind(view))
    }

    override fun onBindViewHolder(holder: QuoteListAdapter.CustomViewHolder, position: Int) {
        holder.bind(quoteList[position])
    }

    override fun getItemCount(): Int = quoteList.size



}
