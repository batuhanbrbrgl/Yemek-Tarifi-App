package com.example.yemektariflerisqlite

import android.os.Bundle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmStatic

public data class TarifFragmentArgs(
  public val bilgi: String = "menudengeldi",
  public val id: Int = 0
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("bilgi", this.bilgi)
    result.putInt("id", this.id)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): TarifFragmentArgs {
      bundle.setClassLoader(TarifFragmentArgs::class.java.classLoader)
      val __bilgi : String?
      if (bundle.containsKey("bilgi")) {
        __bilgi = bundle.getString("bilgi")
        if (__bilgi == null) {
          throw IllegalArgumentException("Argument \"bilgi\" is marked as non-null but was passed a null value.")
        }
      } else {
        __bilgi = "menudengeldi"
      }
      val __id : Int
      if (bundle.containsKey("id")) {
        __id = bundle.getInt("id")
      } else {
        __id = 0
      }
      return TarifFragmentArgs(__bilgi, __id)
    }
  }
}
