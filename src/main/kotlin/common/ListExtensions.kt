package common

fun <T> List<T>.splitOn(pred: (T) -> Boolean): List<List<T>> {
    val data = this.iterator()
    val result: MutableList<List<T>> = mutableListOf()
    var group: MutableList<T> = mutableListOf()
    while (data.hasNext()) {
        val t = data.next()
        if (pred(t)) {
            result.add(group)
            group = mutableListOf()
        } else {
            group.add(t)
        }
    }
    if (result.isNotEmpty()) {
        result.add(group)
    }
    return result
}