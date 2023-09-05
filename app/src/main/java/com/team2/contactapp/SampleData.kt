package com.team2.contactapp

object SampleData {
    val userList = arrayListOf<User>(
        User("짱구",R.drawable.ic_launcher_foreground,"010-1234-5678","testemail01@gmail.com","",""),
        User("철수",R.drawable.ic_launcher_foreground,"010-1234-5678","testemail01@gmail.com","",""),
        User("훈이",R.drawable.ic_launcher_foreground,"010-1234-5678","testemail01@gmail.com","",""),
        User("맹구",R.drawable.ic_launcher_foreground,"010-1234-5678","testemail01@gmail.com","",""),
        User("유리",R.drawable.ic_launcher_foreground,"010-1234-5678","testemail01@gmail.com","",""),
        User("신형만",R.drawable.ic_launcher_foreground,"010-1234-5678","testemail01@gmail.com","",""),
        User("복미선",R.drawable.ic_launcher_foreground,"010-1234-5678","testemail01@gmail.com","",""),
        User("흰둥이",R.drawable.ic_launcher_foreground,"010-1234-5678","testemail01@gmail.com","",""),
        User("장미",R.drawable.ic_launcher_foreground,"010-1234-5678","testemail01@gmail.com","",""),
        User("치타",R.drawable.ic_launcher_foreground,"010-1234-5678","testemail01@gmail.com","",""),
    )

    fun addUser(user: User) { userList.add(user) }
}