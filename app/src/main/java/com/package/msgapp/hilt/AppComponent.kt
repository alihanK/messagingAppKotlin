package com.package.msgapp.hilt

import com.package.msgapp.repository.ChatRepository
import com.package.msgapp.repository.AuthRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseModule::class])
interface AppComponent {
    // ChatRepository için
    fun inject(repository: ChatRepository)
    fun getChatRepository(): ChatRepository

    // AuthRepository için provision metodu
    fun getAuthRepository(): AuthRepository
}
