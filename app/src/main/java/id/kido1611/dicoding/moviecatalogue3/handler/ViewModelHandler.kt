package id.kido1611.dicoding.moviecatalogue3.handler

interface ViewModelHandler {
    fun onSuccess()
    fun onFailure(message: String)
    fun onInit()
}