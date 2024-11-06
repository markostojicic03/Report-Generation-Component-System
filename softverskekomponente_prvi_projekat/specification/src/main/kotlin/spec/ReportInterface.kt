package spec

interface ReportInterface {
    abstract val imlementationName: String
    abstract val fileName:String
    abstract val formattingFlag: Boolean

    /**
     * funkcija za generisanje izvestaja
     * funkcija save
     * prepare data(za bazu, za listu u listi, mapu)
     * formatiranje(ukljuciti razlicite vrste formatiranja, kao sto su bojenje, kolona, red, linija, bold, italic itd.)
     * dodati abstact polje za header i za rezime?
     * dodati podatak za redni broj reda
     * funkcija za racunanje razmak izmedju redova/kolona(properties)
     * kalkulacije(sum, average, count, mnozenje, deljenje, oduzimanje) - proveriti na koji nacin se rade kalkulacije
     * */

}