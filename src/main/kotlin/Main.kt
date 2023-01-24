import jdk.internal.jimage.decompressor.CompressIndexes.readInt
import kotlin.random.Random

/***********************************************************************************************************************
 *  CLASE: Dado
 *  CONSTRUCTOR:
 *      numMin    - > límite inferior del rango del dado
 *      numMax    - > límite superior del rango del dado
 *
 *  METODOS
 *      tirada()  - > Devuelve el valor (Int) aleatorio entre 1 y 6
 **********************************************************************************************************************/
class Dado() {
    private var numMin = 1
    private var numMax = 6

    fun tirada(): Int {
        return Random.nextInt(numMin, numMax)
    }
}




/***********************************************************************************************************************
 *  CLASE: Articulo
 *  CONSTRUCTOR:
 *      id      - > Nombre del artículo
 *      peso    - > Peso del artículo
 *      valor   - > Valor del artículo
 *
 *  METODOS
 *      getPeso()       - > Devuelve el peso como Int
 *      getValor()      - > Devuelve el valor como Int
 *      getId()         - > Devuelve el nombre del artículo como String
 *      toString()      - > Sobreescribimos el método toString para darle formato a la visualización de los artículos
 **********************************************************************************************************************/
class Articulo(private var id: String, private var peso: Int, private var valor: Int) {

    fun getPeso(): Int {
        return peso
    }

    fun getValor(): Int {
        return valor
    }

    fun getId(): String {
        return id
    }

    override fun toString(): String {
        return "[ID:$id, Peso:$peso, Valor:$valor]"
    }
}

/***********************************************************************************************************************
 *  CLASE: Mochila
 *  CONSTRUCTOR:
 *      pesoMochila      - > Peso máximo que puede soportar la mochila
 *
 *  METODOS
 *      getPesoMochila()        - > Devuelve el peso máximo como Int
 *      addArticulo()           - > Añade un artículo (articulo) a la mochila, comprobando el límite
 *      getContenido()          - > Devuelve el ArrayList de artículos que contiene la mochila
 *      findObjeto()            - > Devuelve la posición del artículo que pasamos como entrada o -1 si no lo encuentra
 *
 **********************************************************************************************************************/
class Mochila(private var pesoMochila: Int) {
    private var contenido = ArrayList<Articulo>()

    fun getPesoMochila(): Int {
        return pesoMochila
    }

    fun addArticulo(articulo: Articulo) {
        if (articulo.getPeso() <= pesoMochila) {
            contenido.add(articulo)
            this.pesoMochila -= articulo.getPeso()
        } else {
            println("La mochila está llena, debes vender artículos")
        }
        println("Peso restante de la Mochila: " + pesoMochila)

    }

    fun getContenido(): ArrayList<Articulo> {
        return contenido
    }

    fun findObjeto(id: String): Int {
        for ((indice, item) in contenido.withIndex()) {
            if (item.getId() == id) {
                return indice
            }
        }
        return -1
    }
}

/***********************************************************************************************************************
 *  CLASE: Personaje
 *  CONSTRUCTOR:
 *      nombre          - > Nombre del personaje
 *      pesoMochila     - > Peso máximo que puede soportar la mochila
 *      estadoVital     - > Sólo puede tomar los valores Adulto, Joven o Anciano
 *      raza            - > Sólo puede tomar los valores Enano, Elfo, Humano y Goblin
 *      clase           - > Sólo puede tomar los valores Mercader, Ladrón, Guerrero, Mago y Berserker
 *
 *
 *  ATRIBUTOS:
 *      mochila         - > Cada personaje dispone de una mochila con un límite de peso establecido en el constructor
 *      monedero        - > Cada personaje dispone de un monedero con capacidad para coins de 1, 5, 10, 25 y 100
 *
 *  METODOS
 *      get/setNombre()         - > Devuelve/Establece el nombre del Personaje
 *      get/setEstadoVital()    - > Devuelve/Establece el estado vital del Personaje
 *      get/setRaza()           - > Devuelve/Establece la raza del Personaje
 *      get/setClase()          - > Devuelve/Establece la clase del Personaje
 *      getMochila              - > Devuelve/Establece la mochila del Personaje
 *
 *      cifrado                 - > Param. Entrada: <Texto a crifrar> <ROT>
 *      comunicacion            - > Param. Entrada: <Mensaje para el usuario>
 *      vender                  - > Param. Entrada: <Personaje Mercader> <Articulo a vender>
 *                                  Descripción: método que comprueba si el Personaje pasado como parámetro de entrada
 *                                  es Mercader o no. Asimismo, comprueba el contenido de la mochila y si en él se
 *                                  encuentra el Artículo pasado como parámetro de entrada. De ser así, lo mueve a la
 *                                  mochila del mercader, realiza la transacción económica y lo elimina de la mochila
 *                                  del Personaje.
 *
 *      cashConverter           - > Param. Entrada: <Articulo>
 *                                  Descripción: método que transforma el valor del Artículo en monedas.
 *
 **********************************************************************************************************************/
//Los pongo publicos porque es más fácil de gestionar (asumiendo el peligro de liarla, porque están desprotegidos)
class Personaje(
    var nombre: String,
    var pesoMochila: Int,
    var estadoVital: String,
    var raza: String,
    var clase: String,
    var fuerza: Int,
    var destreza: Int,
    var constitucion: Int,
    var inteligencia: Int,
    var sabiduria: Int,
    var carisma: Int

) {

    private var mochila = Mochila(pesoMochila)
    var monedero = HashMap<Int, Int>()

    //Inicializo con mi función calcularHabilidad, que cumple el primer punto del ejercicio,
    //lanza 4 dados y se queda con las 3 puntuaciones más altas
    init {
        fuerza = calcularHabilidad()
        destreza = calcularHabilidad()
        //a constitución le sumo 10 y lo divido entre 2
        constitucion = (((calcularHabilidad())+10)/2).toInt()
        inteligencia = calcularHabilidad()
        sabiduria = calcularHabilidad()
        carisma = calcularHabilidad()
    }

    //Dependiendo de la raza, le sumamos X puntos a cada habilidad
    init {
        when (raza) {
            "Humano" -> fuerza += 5
            "Humano" -> destreza += 5
            "Humano" -> constitucion += 5
            "Humano" -> inteligencia += 5

            "Elfo" -> sabiduria += 7
            "Elfo" -> destreza += 7
            "Elfo" -> inteligencia += 7

            "Enano" -> fuerza += 10
            "Enano" -> constitucion += 10
            "Enano" -> destreza += 10

            "Goblin" -> destreza += 8
            "Goblin" -> fuerza += 8;
            "Goblin" -> carisma += 8;
        }
    }

    init {
        monedero.put(1, 0)
        monedero.put(5, 0)
        monedero.put(10, 0)
        monedero.put(25, 0)
        monedero.put(100, 0)
    }

    /*fun getNombre(): String {
        return nombre
    }

    fun setNombre(nombre: String) {
        this.nombre = nombre
    }

    fun getEstadoVital(): String {
        return estadoVital
    }

    fun setEstadoVital(estadoVital: String) {
        this.estadoVital = estadoVital
    }

    fun getRaza(): String {
        return raza
    }

    fun setRaza(raza: String) {
        this.raza = raza
    }

    fun getClase(): String {
        return clase
    }

    fun setClase(clase: String) {
        this.clase = clase
    }

    fun getMochila(): Mochila {
        return this.mochila
    }*/

    fun calcularHabilidad(): Int {
        var tirada = 0
        var arrayTirada = arrayOf(0, 0, 0, 0)
        var resultado = 0
        for (i in 1..4) {
            tirada = Dado().tirada()
            arrayTirada.set(i, tirada)
        }
        arrayTirada.sortDescending()
        for (i in 1..3) {
            resultado += arrayTirada[i]
        }
        return resultado
    }


    fun cifrado(mensaje: String, ROT: Int): String {
        val abecedario: ArrayList<Char> = "abcdefghijklmnñopqrstuvwxyz".toList() as ArrayList<Char>
        var stringInv = ""
        var indice = 0

        for (i in mensaje.lowercase().toList() as ArrayList<Char>) {
            if (!i.isLetter() || i.isWhitespace()) {
                stringInv += i
            } else {
                indice = abecedario.indexOf(i) + ROT
                if (indice >= abecedario.size) {
                    indice -= abecedario.size
                    stringInv += abecedario[indice]
                } else
                    stringInv += abecedario[indice]
            }
        }
        return stringInv.filter { !it.isWhitespace() && it.isLetter() }
    }

    fun comunicacion(mensaje: String) {
        var respuesta = ""
        when (estadoVital) {
            "Adulto" -> {
                if (mensaje.equals("¿Como estas?"))
                    respuesta = "En la flor de la vida, pero me empieza a doler la espalda"
                else
                    if ((mensaje.contains('?') || mensaje.contains('¿')) && mensaje == mensaje.uppercase())
                        respuesta = "Estoy buscando la mejor solución"
                    else
                        if (mensaje == mensaje.uppercase())
                            respuesta = "No me levantes la voz mequetrefe"
                        else
                            if (mensaje == nombre)
                                respuesta = "¿Necesitas algo?"
                            else
                                if (mensaje == "Hasta la próxima luchadores")
                                    respuesta = "Un placer servirle en idioma adulto"
                                else
                                    respuesta = "No sé de qué me estás hablando"


            }

            "Joven" -> {
                if (mensaje.equals("¿Como estas?"))
                    respuesta = "De lujo"
                else
                    if ((mensaje.contains('?') || mensaje.contains('¿')) && mensaje == mensaje.uppercase())
                        respuesta = "Tranqui se lo que hago"
                    else
                        if (mensaje == mensaje.uppercase())
                            respuesta = "Eh relájate"
                        else
                            if (mensaje == nombre)
                                respuesta = "Qué pasa?"
                            else
                                if (mensaje == "Hasta la próxima luchadores")
                                    respuesta = "Un placer servirle en idioma joven"
                                else
                                    respuesta = "Yo que se"

            }

            "Anciano" -> {
                if (mensaje.equals("¿Como estas?"))
                    respuesta = "No me puedo mover"
                else
                    if ((mensaje.contains('?') || mensaje.contains('¿')) && mensaje == mensaje.uppercase())
                        respuesta = "Que no te escucho!"
                    else
                        if (mensaje == mensaje.uppercase())
                            respuesta = "Háblame más alto que no te escucho"
                        else
                            if (mensaje == nombre)
                                respuesta = "Las 5 de la tarde"
                            else
                                if (mensaje == "Hasta la próxima luchadores")
                                    respuesta = "Un placer servirle en idioma adulto"
                                else
                                    respuesta = "En mis tiempos esto no pasaba"
            }
        }
        when (raza) {
            "Elfo" -> println(cifrado(respuesta, 1))
            "Enano" -> println(respuesta.uppercase())
            "Goblin" -> println(cifrado(respuesta, 1))
            else -> println(respuesta)
        }
    }

    fun vender(mercader: Personaje, articulo: Articulo) {
        var id: String
        var posicion: Int
        if (!mercader.clase.equals("Mercader"))
            print("No soy un Mercader")
        else {
            if (this.mochila.getContenido().size != 0) {
                println("Tienes ${this.mochila.getContenido().size} objetos")
                mochila.getContenido().forEach { println(it) }
                posicion = mochila.findObjeto(articulo.getId())
                if (posicion != -1) {
                    println("Movemos el artículo al mercader")
                    mercader.mochila.addArticulo(mochila.getContenido()[posicion])
                    println("Convertimos el artículo en monedas")
                    cashConverter(mochila.getContenido()[posicion])
                    println("Eliminamos el artículo del jugador principal")
                    this.mochila.getContenido().remove(mochila.getContenido()[posicion])
                } else println("No hay ningún objeto con ese ID")

            } else println("No tienes objetos para vender")

            println("Te quedan ${this.mochila.getContenido().size} objetos")
            println("Hasta pronto")
        }
    }

    fun cashConverter(articulo: Articulo) {
        var total = 0
        var i = 0
        var coins = arrayListOf(1, 5, 10, 25, 100)
        coins.sortDescending()

        while (total < articulo.getValor() && i < coins.size) {
            if (total + coins[i] <= articulo.getValor()) {
                total += coins[i]
                monedero[coins[i]] = monedero[coins[i]]!! + 1
            } else
                i++
        }
    }

}

fun main() {
    var listaPersonajes: MutableList<Personaje> = mutableListOf()
    var numPersonajes = 0
    println("Dime cuantos personajes vas a introducir")
    numPersonajes = readLine()?.toInt()!!
    var sumLuchadores = 0
    var sumMercader = 0
    if (numPersonajes < 4) {
        print("Tienes que introducir por lo menos 3 jugadores (2 luchadores + 1 mercader")
    } else {
        for (i in 1..numPersonajes!!) {
            println("Dime nombre: ")
            listaPersonajes[i].nombre = readLine().toString()
            println("Dime peso mochila")
            listaPersonajes[i].pesoMochila = readLine()?.toInt()!!
            println("Dime estado vital: ")
            listaPersonajes[i].estadoVital = readLine().toString()
            println("Dime raza: ")
            listaPersonajes[i].raza = readLine().toString()
            println("Dime clase: ")
            listaPersonajes[i].clase = readLine().toString()
            if (listaPersonajes[i].clase.equals("luchador")) {
                sumLuchadores += 1
            }
            if (listaPersonajes[i].clase.equals("mercader")) {
                sumMercader += 1
            }
        }
        if (sumLuchadores < 2) {
            ("Error, tienes que tener por lo menos 2 luchadores")
        }
        if (sumMercader < 1) {
            ("Error, tienes que tener por lo menos 1 mercader")
        }
    }
}