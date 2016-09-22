
@Grapes( @Grab('com.xlson.groovycsv:groovycsv:1.0') )
import com.xlson.groovycsv.*
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.regex.Pattern;
import groovy.io.FileType

def dir = new File("categories")
dir.eachFileRecurse (FileType.FILES) { csvFile ->

    println "Scraping ${csvFile.name}..."

    def data = new CsvParser().parse(new FileReader(csvFile))
    List productList = []

    data.each { row ->
        Map product = [:]
        row.columns.each{ key, value ->
            product[key] = row[key]
        }
        productList << product
    }

        String category = "synergy"

        productList.eachWithIndex { product, index ->
        "mkdir -p out/".execute()

        File file = new File("out/" + slugify(product.productName) +'.md')

        println "out/" +slugify(product.productName) +'.md'
        file.write ('---\n')

//        file << "slugID: $counter \n"
        file << "\n"
        file << "category: \"$category\"\n"
        file << "name: \"$product.productName\"\n"
        file << "price: \"$product.price\"\n"
        file << "link: \"$product.link\"\n"
        file << "image: \"$product.image\"\n"
        file << "type: \n"
        file << '---' << '\n'
    }
}

public String slugify (String toBeSlugged){
    Pattern NONLATIN = Pattern.compile("[^\\w-]")
    Pattern WHITESPACE = Pattern.compile("[\\s]")

    String nowhitespace = WHITESPACE.matcher(toBeSlugged.replaceAll(" - ", " ")).replaceAll("-")
    String normalized = Normalizer.normalize(nowhitespace, Form.NFD)
    String slug = NONLATIN.matcher(normalized).replaceAll("")
    return slug.toLowerCase(Locale.ENGLISH)
}

