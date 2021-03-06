import java.io.File

val inputFile = File(if (!args.isEmpty()) args[0] else "input")
if (!inputFile.exists() || !inputFile.isFile()) {
    println("input file with name '${inputFile.getPath()}' does not exist or is not a file")
    System.exit(1)
}

data class NumberList(val size: Int, var startIdx: Int, var skipSize: Int) {
    val stream: MutableList<Int>

    init {
        stream = (0..size-1).toMutableList()
    }

    fun swap(index1: Int, index2: Int) {
        stream[index1] = stream[index2].also { stream[index2] = stream[index1] }
    }

    fun incStartIndex(length: Int): NumberList {
        startIdx = (startIdx+length+skipSize)%size
        skipSize += 1
        return this
    }
}

val numberList = inputFile
    .readText()
    .split(",")
    .map { it.trim().toInt() }
    .fold(NumberList(256, 0, 0), { numberList, length ->
        (0..(length-1)/2).forEach {
            numberList.swap((numberList.startIdx+it)%numberList.size, (numberList.startIdx+length-1-it)%numberList.size)
        }
        
        numberList.incStartIndex(length)
    })

val firstTwoMult = numberList.stream[0]*numberList.stream[1]

println("the result of multiplying the first two elements of the number list is '$firstTwoMult'")

val sparseHashNumberList = (0..63)
    .fold(NumberList(256, 0, 0), { numberList, _ ->
        (inputFile
            .readText()
            .trim()
            .toCharArray()
            .map { it.toInt() } + listOf(17, 31, 73, 47, 23))
            .forEach { length ->
                (0..(length-1)/2).forEach {
                    numberList.swap((numberList.startIdx+it)%numberList.size, (numberList.startIdx+length-1-it)%numberList.size)
                }

                numberList.incStartIndex(length)
            }
        
        numberList
    })

val denseHash = sparseHashNumberList
    .stream
    .withIndex()
    .groupBy { it.index / 16 }
    .map { it.value.map { it.value } }
    .map { it.fold(0, { r, x -> r.xor(x) }) }
    .map { it.toString(16).padStart(2, '0') }
    .joinToString("")

println("the hexadecimal representation of the dense hash of the input is '$denseHash'")
