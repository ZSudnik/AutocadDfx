package zibi.robotx.autocad.data.dxf.protocol

import zibi.robotx.autocad.data.dxf.DxfChain
import zibi.robotx.autocad.data.dxf.DxfLoaderContext
import zibi.robotx.autocad.data.dxf.protocol.entity.*
import zibi.robotx.autocad.data.dxf.util.*
import javax.vecmath.Vector3f

class BlkBlock(lastElem: DxfChain, isRead: Boolean) : DxfChain() {

    //        5     Handle
    override var HandlenotOmitted: Int? = null
    //        102     Start of application-defined group “{application_name”. For example, “{ACAD_REACTORS” indicates the start of the AutoCAD persistent reactors group (optional)        application-defined codes                 Codes and values within the 102 groups are application defined (optional)
    var ApplicationName: String? = null
    //        102         End of group, “}” (optional)
    var EndOfGroup: String? = null
    //        330         Soft-pointer ID/handle to owner object
    var PointerId: String? = null
    //        100         Subclass marker (AcDbEntity)
    var subclassMarkerBlockEntity: String? = null
    //        8         Layer name
    var LayerName: String? = null
    //        100         Subclass marker (AcDbBlockBegin)
    var subclassMarkerBlockBegin: String? = null
    //        2         Block name
    var BlockName: String = ""
        private set
    //        70         Block-type flags (bit-coded values, may be combined):
    //            0 = Indicates none of the following flags apply
    //            1 = This is an anonymous block generated by hatching, associative dimensioning, other internal operations, or an application
    //            2 = This block has non-constant attribute definitions (this bit is not set if the block has any attribute definitions that are constant, or has no attribute definitions at all)
    //            4 = This block is an external reference (xref)
    //            8 = This block is an xref overlay
    //            16 = This block is externally dependent
    //            32 = This is a resolved external reference, or dependent of an external reference (ignored on input)
    //            64 = This definition is a referenced external reference (ignored  on input)
    var Flag: Int? = null
    //        10         Base point         DXF: X value; APP: 3D point
    //        20, 30         DXF: Y and Z values of base point
    var BasePoint: Vector3f? = null
    //        3         Block name
    var BlockName2: String? = null
    //        1         Xref path name
    var PathNameXref: String? = null
    //        4         Block description (optional)
    var BlockDescription: String? = null





    init {
        last(lastElem)
        if (isRead) read(lastElem.dxfContext)
    }

    //only critical situation
    constructor(lastElem: DxfChain, BlockName: String): this( lastElem, false) {
        this.BlockName = BlockName
    }

    override fun read(dxfContext: DxfLoaderContext) {
        var code = dxfContext.get()
        while (true) {
            if (dxfContext.codEquals("0")){
                do{ code = dxfContext.get() }while(code == "0")
                when( code) {
                    "ENDBLK" -> return
                    "LINE" -> { EntLINE( this, true); continue }
                    "LWPOLYLINE" -> { EntLWPOLYLINE( this, true); continue }
                    "POLYLINE" -> { EntPOLYLINE( this, true); continue }
                    "CIRCLE" -> { EntCIRCLE( this, true); continue }
                    "ELLIPSE" -> { EntELLIPSE( this, true); continue }
                    "ARC" -> { EntARC( this, true); continue }
                    "SPLINE"  -> { EntSPLINE( this, true); continue }
                    "INSERT" -> { EntINSERT( this, true); continue } //by AutoCAD "Block definitions are never nested"
                    "HATCH" -> { EntHATCH( this, true); continue } //TODO
                    "POINT" -> { EntPOINT( this, true); continue } //TODO
                    "MTEXT" -> { EntMTEXT( this, true); continue } //TODO
//                    else -> println(" RESt  $code")
                }
            }
            when(code) {
//                super.readX( cod ) -> {
//                }
                "5"  -> {
                    HandlenotOmitted = dxfContext.intHexValue()
                }
                "102"  -> {
                    ApplicationName = dxfContext.stringValue()
                }
                "102"  -> {
                    EndOfGroup = dxfContext.stringValue()
                }
                "330"  -> {
                    PointerId = dxfContext.stringValue()
                }
                "100"  -> {
                    subclassMarkerBlockEntity = dxfContext.stringValue()
                }
                "8"  -> {
                    LayerName = dxfContext.stringValue()
                }
                "100"  -> {
                    subclassMarkerBlockBegin = dxfContext.stringValue()
                }
                "2"  -> {
                    BlockName = dxfContext.stringValue()
                }
                "70"  -> {
                    Flag = dxfContext.intValue()
                }
                "10"  -> {
                    if( BasePoint == null) BasePoint = Vector3f()
                    BasePoint?.x = dxfContext.floatValue()
                }
                "20"  -> {
                    if( BasePoint == null) BasePoint = Vector3f()
                    BasePoint?.y = dxfContext.floatValue()
                }
                "30"  -> {
                    if( BasePoint == null) BasePoint = Vector3f()
                    BasePoint?.z = dxfContext.floatValue()
                }
                "3"  -> {
                    BlockName2 = dxfContext.stringValue()
                }
                "1"  -> {
                    PathNameXref = dxfContext.stringValue()
                }
                "4"  -> {
                    BlockDescription = dxfContext.stringValue()
                }
            }
            code = dxfContext.get()
        }
    }

    override fun write(sbX: StringBuilder): StringBuilder {
        sbX.append("\n 0\nBLOCK")
        if( BasePoint != null) sbX.append( "\n 10\n"+BasePoint?.x+"\n 20\n"+BasePoint?.y+"\n 30\n"+BasePoint?.z )
        if( HandlenotOmitted != null) sbX.append( "\n 5\n"+HandlenotOmitted.toHex() )
        if( PointerId != null) sbX.append( "\n 330\n"+PointerId )
        if( LayerName != null) sbX.append( "\n 8\n"+LayerName )
        if( subclassMarkerBlockEntity != null) sbX.append( "\n 100\n"+subclassMarkerBlockEntity )
        if( BlockName != null) sbX.append( "\n 2\n"+BlockName )
        if( Flag != null) sbX.append( "\n 70\n"+Flag )
        if( BlockName2 != null) sbX.append( "\n 3\n"+BlockName2 )
        if( PathNameXref != null) sbX.append( "\n 1\n"+PathNameXref )
        if( BlockDescription != null) sbX.append( "\n 2\n"+BlockDescription )

        sbX.append( "\n 0\nENDBLK")

        return sbX
    }


//    fun get3DFACE(): Ent3DFACE {
//        return Ent3DFACE( this, false)
//    }
//    fun get3DSOLID(): Ent3DSOLID {
//        return Ent3DSOLID( this, false)
//    }
//    fun getACAD_PROXY_ENTITY(): EntACAD_PROXY_ENTITY {
//        return EntACAD_PROXY_ENTITY( this, false)
//    }
    fun getARC(): EntARC {
        return EntARC( this, false)
    }
//    fun getATTDEF(): EntATTDEF {
//        return EntATTDEF( this, false)
//    }
//    fun getATTRIB(): EntATTRIB {
//        return EntATTRIB( this, false)
//    }
//    fun getBODY(): EntBODY {
//        return EntBODY( this, false)
//    }
    fun getCIRCLE(): EntCIRCLE {
        return EntCIRCLE( this, false)
    }
//    fun getCOORDINATION_MODEL(): EntCOORDINATION_MODEL {
//        return EntCOORDINATION_MODEL( this, false)
//    }
//    fun getDimensionCommon(): EntDimensionCommon {
//        return EntDimensionCommon( this, false)
//    }
//    fun getDimensionAligned(): EntDimensionAligned  {
//        return EntDimensionAligned( this, false)
//    }
//    fun getDimensionAngular(): EntDimensionAngular {
//        return EntDimensionAngular( this, false)
//    }
//    fun getDimensionLinearAndRotated(): EntDimensionLinearAndRotated {
//        return EntDimensionLinearAndRotated( this, false)
//    }
//    fun getDimensionOrdinate(): EntDimensionOrdinate {
//        return EntDimensionOrdinate( this, false)
//    }
//    fun getDimensionRadialAndDiameter(): EntDimensionRadialAndDiameter {
//        return EntDimensionRadialAndDiameter( this, false)
//    }
    fun getELLIPSE(): EntELLIPSE {
        return EntELLIPSE( this, false)
    }
    fun getHATCH(): EntHATCH {
        return EntHATCH( this, false)
    }
//    fun getHELIX(): EntHELIX {
//        return EntHELIX( this, false)
//    }
//    fun getIMAGE(): EntIMAGE {
//        return EntIMAGE( this, false)
//    }
    fun getINSERT(): EntINSERT {
        return EntINSERT( this, false)
    }
//    fun getLEADER(): EntLEADER {
//        return EntLEADER( this, false)
//    }
//    fun getLIGHT(): EntLIGHT {
//        return EntLIGHT( this, false)
//    }
    fun getLINE(): EntLINE {
        return EntLINE( this, false)
    }
    fun getLWPOLYLINE(): EntLWPOLYLINE {
        return EntLWPOLYLINE( this, false)
    }
//    fun getMESH(): EntMESH {
//        return EntMESH( this, false)
//    }
//    fun getMLINE(): EntMLINE {
//        return EntMLINE( this, false)
//    }
//    fun getMTEXT(): EntMTEXT {
//        return EntMTEXT( this, false)
//    }
//    fun getOLEFRAME(): EntOLEFRAME {
//        return EntOLEFRAME( this, false)
//    }
//    fun getOLE2FRAME(): EntOLE2FRAME {
//        return EntOLE2FRAME( this, false)
//    }
//    fun getPOINT(): EntPOINT {
//        return EntPOINT( this, false)
//    }
    fun getPOLYLINE(): EntPOLYLINE {
        return EntPOLYLINE( this, false)
    }
//    fun getRAY(): EntRAY {
//        return EntRAY( this, false)
//    }
//    fun getREGION(): EntREGION {
//        return EntREGION( this, false)
//    }
//    fun getSHAPE(): EntSHAPE {
//        return EntSHAPE( this, false)
//    }
//    fun getSOLID(): EntSOLID {
//        return EntSOLID( this, false)
//    }
    fun getSPLINE(): EntSPLINE {
        return EntSPLINE( this, false)
    }
//    fun getSUN(): EntSUN {
//        return EntSUN( this, false)
//    }
//    fun getSURFACE(): EntSURFACE {
//        return EntSURFACE( this, false)
//    }
//    fun getSurfaceExtruded(): EntSurfaceExtruded {
//        return EntSurfaceExtruded( this, false)
//    }
//    fun getSurfaceLofted(): EntSurfaceLofted {
//        return EntSurfaceLofted( this, false)
//    }
//    fun getSurfaceRevolved(): EntSurfaceRevolved {
//        return EntSurfaceRevolved( this, false)
//    }
//    fun getSurfaceSwept(): EntSurfaceSwept {
//        return EntSurfaceSwept( this, false)
//    }
//    fun getTABLE(): EntTABLE {
//        return EntTABLE( this, false)
//    }
    fun getTEXT(): EntTEXT {
        return EntTEXT( this, false)
    }
//    fun getTOLERANCE(): EntTOLERANCE {
//        return EntTOLERANCE( this, false)
//    }
//    fun getTRACE(): EntTRACE {
//        return EntTRACE( this, false)
//    }
//    fun getUNDERLAY(): EntUNDERLAY {
//        return EntUNDERLAY( this, false)
//    }
//    fun getVERTEX(): EntVERTEX {
//        return EntVERTEX( this, false)
//    }
//    fun getVIEWPORT(): EntVIEWPORT {
//        return EntVIEWPORT( this, false)
//    }
//    fun getWIPEOUT(): EntWIPEOUT {
//        return EntWIPEOUT( this, false)
//    }
//    fun getXLINE(): EntXLINE {
//        return EntXLINE( this, false)
//    }


    override fun centerObject(sizeMMParent: SizeMinMax?): SizeMinMax? {
        return sizeMMParent
    }

    override fun scaleObjectToFit(maxRadiusSqr: Float): Float {
        return maxRadiusSqr
    }

    override fun collectionConnect(collectionPoint: CollectionPoint): Unit {
        // collectionPoint
    }

    override fun xdef(): Int {
        return 0
    }

    fun copyInto(blk: BlkBlock) {
        blk.HandlenotOmitted = HandlenotOmitted
        blk.ApplicationName = ApplicationName
        blk.EndOfGroup = EndOfGroup
        blk.PointerId = PointerId
        blk.subclassMarkerBlockEntity = subclassMarkerBlockEntity
        blk.LayerName = LayerName
        blk.subclassMarkerBlockBegin = subclassMarkerBlockBegin
        blk.BlockName = BlockName
        blk.Flag = Flag
        blk.BasePoint = BasePoint
        blk.BlockName2 = BlockName2
        blk.PathNameXref = PathNameXref
        blk.BlockDescription = BlockDescription
    }

    companion object{
        var ile = 0
    }
}