package com.amrwalidi.android.fancup.domain

import android.os.Parcel
import android.os.Parcelable

data class Question(
    val id: Long,
    val listId: String = "",
    val text: String,
    val answers: List<String>,
    val options: List<String>,
    val type: Int,
    val difficulty: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(listId)
        parcel.writeString(text)
        parcel.writeStringList(answers)
        parcel.writeStringList(options)
        parcel.writeInt(type)
        parcel.writeInt(difficulty)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}

