package com.example.mvvmwithretrofit.repository

import com.example.mvvmwithretrofit.models.QuoteList

//sealed class Response(){ //One way to Add Response class for Error Handling without generic
//    class Loading: Response()
//    class Success(val quoteList: QuoteList): Response()
//    class Error(val errorMessage: String): Response()
//}

//Other way to Add Response class for Error Handling without generic
//sealed class Response2(val data: QuoteList? = null, val errorMessage: String? =null){
//    class Loading: Response2()
//    class Success(quoteList: QuoteList): Response2(data = quoteList)
//    class Error(errorMessage: String): Response2(errorMessage = errorMessage)
//}

//Generic Implementation for different DataTye
sealed class Response<T>(val data: T? = null, val errorMessage: String? =null){
    class Loading<T>: Response<T>()
    class Success<T>(data: T?= null): Response<T>(data = data)
    class Error<T>(errorMessage: String): Response<T>(errorMessage = errorMessage)
}