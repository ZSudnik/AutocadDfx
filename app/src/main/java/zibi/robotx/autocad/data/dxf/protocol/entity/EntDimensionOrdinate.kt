package zibi.robotx.autocad.data.dxf.protocol.entity

import zibi.robotx.autocad.data.dxf.DxfChain
import zibi.robotx.autocad.data.dxf.DxfLoaderContext
import zibi.robotx.autocad.data.dxf.util.*
import javax.vecmath.Vector3f

class EntDimensionOrdinate(lastElem: DxfChain, isRead: Boolean) : EntCommon() {



	//	 Definition point for linear and angular dimensions (in WCS), DXF: X value APP: 3D pointDXF: Y and Z values of definition point for linear and angular dimensions (in WCS)
	//	13	23	33	
	var DefinitionPoint: Vector3f? = null

	//	 Definition point for linear and angular dimensions (in WCS), DXF: X value APP: 3D pointDXF: Y and Z values of definition point for linear and angular dimensions (in WCS)
	//	14	24	34	
	var DefinitionPoint1: Vector3f? = null

    override fun read(getbfr: DxfLoaderContext) {
    var cod = getbfr.get()
    while (true) {
        if (cod == "0") break
        if (super.readX(cod)) {
            when (cod) {
			"13" -> {
				if( DefinitionPoint == null ) DefinitionPoint = Vector3f()
				DefinitionPoint?.x = getbfr.floatValue()
			}
			"23" -> {
				if( DefinitionPoint == null ) DefinitionPoint = Vector3f()
				DefinitionPoint?.y = getbfr.floatValue()
			}
			"33" -> {
				if( DefinitionPoint == null ) DefinitionPoint = Vector3f()
				DefinitionPoint?.z = getbfr.floatValue()
			}
			"14" -> {
				if( DefinitionPoint1 == null ) DefinitionPoint1 = Vector3f()
				DefinitionPoint1?.x = getbfr.floatValue()
			}
			"24" -> {
				if( DefinitionPoint1 == null ) DefinitionPoint1 = Vector3f()
				DefinitionPoint1?.y = getbfr.floatValue()
			}
			"34" -> {
				if( DefinitionPoint1 == null ) DefinitionPoint1 = Vector3f()
				DefinitionPoint1?.z = getbfr.floatValue()
			}
			}
		}
			cod = getbfr.get()
	}
	}


    override fun centerObject(sizeMMParent: SizeMinMax?): SizeMinMax? {
    //    return sizeMMParent?.findMinMax( begpnt, endpnt, xtruDir )
    //            ?: SizeMinMax.findMinMax( begpnt, endpnt, xtruDir )
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

    init {
        last(lastElem)
        if (isRead) {
            read(lastElem.dxfContext)
        }
    }
	 companion object {


	//	 Subclass marker (AcDbOrdinateDimension)
	//	100	
	const val AcDbOrdinateDimension: String = "AcDbOrdinateDimension"
	 }


	 override fun write( sbX: StringBuilder): StringBuilder {
		val sb: StringBuilder = StringBuilder()
		super.write( sb )
		if( DefinitionPoint != null) sb.append( "\n 13\n"+DefinitionPoint?.x+"\n 23\n"+DefinitionPoint?.y+"\n 33\n"+DefinitionPoint?.z )
		if( DefinitionPoint1 != null) sb.append( "\n 14\n"+DefinitionPoint1?.x+"\n 24\n"+DefinitionPoint1?.y+"\n 34\n"+DefinitionPoint1?.z )
		if( sb.isNotEmpty() ) {
 			sbX.append( "\n 0\nDimensionOrdinate")
			sbX.append( sb)
		}

		 return sbX
	}}