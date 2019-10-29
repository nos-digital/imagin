package nl.nos.imagin.example.data

class Repository {
    fun getPictures() = listOf(
        Picture("Mountains", "Mountains.jpg"),
        Picture("Sunset", "Sunset.jpg"),
        Picture("Shanghai", "Shanghai.jpg"),
        Picture("Dolfin", "Dolfin.jpg")
    )
}