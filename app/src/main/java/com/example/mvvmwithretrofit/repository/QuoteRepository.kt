package com.example.mvvmwithretrofit.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmwithretrofit.api.QuoteService
import com.example.mvvmwithretrofit.db.QuoteDatabase
import com.example.mvvmwithretrofit.models.QuoteList
import com.example.mvvmwithretrofit.utils.NetworkUtlis

class QuoteRepository(
    private val quoteService: QuoteService,
    private val quoteDatabase: QuoteDatabase,
    private val applicationContext: Context // to send context to check internet
) {

    private val quotesLiveData = MutableLiveData<Response<QuoteList>>() // Response for Error handling
    //before it Simple <QuoteList> now its Response<QuoteList>

    val quotes: LiveData<Response<QuoteList>>
        get() = quotesLiveData

    suspend fun getQuotes(page: Int){

        if (NetworkUtlis.isInternetAvailable(applicationContext)){ //check internet available or not
            try { // For Error Handling
                val result = quoteService.getQuotes(page)
                if (result?.body() != null){
                    quoteDatabase.quoteDao().addQuotes(result.body()!!.results) //to add quotes on database
                    quotesLiveData.postValue(Response.Success(result.body()))
                    //Response.Success this for error handling only execute when code is success
                }else{
                    quotesLiveData.postValue(Response.Error("Result is null")) //when you get Error
                }
            }catch (e: Exception){
                quotesLiveData.postValue(Response.Error(e.message.toString())) //when you get Error
            }

        }else{
            try { // For Error Handling
                val quotes = quoteDatabase.quoteDao().getQuotes()
                val quoteList = QuoteList(1, 1, 1, quotes, 1, 1)
                quotesLiveData.postValue(Response.Success(quoteList))
            }catch (e: Exception){
                quotesLiveData.postValue(Response.Error(e.message.toString())) //when you get Error
            }
        }

    }

    suspend fun getQuotesBackground(){
        val randomNumber = (Math.random() * 10).toInt()
        val result = quoteService.getQuotes(randomNumber)
        if (result?.body() != null){
            quoteDatabase.quoteDao().addQuotes(result.body()!!.results)
        }
    }
}