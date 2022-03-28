import com.influxdb.client.kotlin.InfluxDBClientKotlin
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import com.influxdb.client.kotlin.QueryKotlinApi
import com.influxdb.query.FluxRecord
import com.influxdb.query.FluxTable
import com.otavi.pl.backend.dataClass.InfluxDbSettings
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.runBlocking


class InfluxDb(val InfluxConfig: InfluxDbSettings = InfluxDbSettings()) {
    private var InfluxDb: InfluxDBClientKotlin
    init {
        InfluxDb = InfluxDBClientKotlinFactory.create(InfluxConfig.ip, InfluxConfig.token.toCharArray(), InfluxConfig.org)
    }


    fun getLastStats():Any {
        InfluxDb = InfluxDBClientKotlinFactory.create(InfluxConfig.ip, InfluxConfig.token.toCharArray(), InfluxConfig.org)
        val ping = InfluxDb.ping()
        InfluxDb.use {

        }


            val queryApi: QueryKotlinApi = InfluxDb.getQueryKotlinApi()
            val queryPing = "from(bucket:\"ts_3\")" +
                    " |> range(start: -5m)" +
                    " |> filter(fn: (r) => r[\"_measurement\"] == \"online_on_ts\")" +
                    " |> filter(fn: (r) => r[\"_field\"] == \"current_ping\")" +
                    " |> last()" +
                    " |> yield(name: \"last\")"
            val resultPing = queryApi.query(queryPing)
            val resultTest = resultPing.consumeAsFlow()
            val sss = resultPing
                .consumeAsFlow()
        println(resultPing)
            val queryPacketLost: String = "from(bucket:\"ts_3\")" +
                    "|> range(start: -5m)" +
                    "|> filter(fn: (r) => r[\"_measurement\"] == \"online_on_ts\")" +
                    "|> filter(fn: (r) => r[\"_field\"] == \"current_packet_loss\")" +
                    "|>last()" +
                    "|> yield(name: \"last\")"
            val resultPacketLost = queryApi.query(queryPacketLost)
            val queryUserOnLine: String =("from(bucket:\"ts_3\")" +
                    " |> range(start: -5m)" +
                    " |> filter(fn: (r) => r[\"_measurement\"] == \"online_on_ts\")" +
                    " |> filter(fn: (r) => r[\"_field\"] == \"current_user_online\")" +
                    " |> last()")
            val tables: Channel<FluxRecord> = InfluxDb.getQueryKotlinApi().query(queryPing)
            //val resultUserOnLine = queryApi.query(queryUserOnLine)
        //return LastServerStats(ping = )
            val resultUserOnLine = queryApi.query(queryUserOnLine)

        return resultUserOnLine
    }
}