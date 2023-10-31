

/*
[10:03] Rastogi, Puneet

Given an integer array nums, return true if any value appears at least twice in the array, and return false if every element is distinct.

[10:03] Rastogi, Puneet

Example 1:

Input: nums = [1,2,3,1]Output: true

[10:03] Rastogi, Puneet

Input: nums = [1,2,3,4]Output: false
 */


class Test {
}

//fun main() {
//    println(checkDuplicate(arrayOf(1,2,3,4)))
//    println(checkDuplicate(arrayOf(1,2,3,1)))
//    assert(true == checkDuplicate(arrayOf(1,2,3,4)), )
//}

fun checkDuplicate(array: Array<Int>) : Boolean {
    val hashSet = HashSet<Int>()
    for (element in array) {
        if (hashSet.add(element) == false) {
            return true
        }
    }
    return false
}

/*
[10:09] Rastogi, Puneet

Given a sorted array and a number x, find the pair in array whose sum is equal to x

[10:10] Rastogi, Puneet

Input: arr[] = {10, 22, 28, 29, 30, 40}, x = 54 Output: 22 and 30
 */

fun main() {
  val array = arrayOf(10, 24, 28, 29, 30, 40)
  print(getPair(array, 54))
}


fun getPair(array: Array<Int>, x : Int): Pair<Int, Int> {
    var left = 0
    var right = array.size - 1
    while (left < right) {
        val sum = array[left] + array[right]
        if (sum == x) {
            return Pair(array[left], array[right])
        } else if (sum > x) {
            right--
        } else {
            left++
        }
    }
    return Pair(-1, -1)
}
