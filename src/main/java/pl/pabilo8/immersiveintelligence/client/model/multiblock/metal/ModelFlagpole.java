package pl.pabilo8.immersiveintelligence.client.model.multiblock.metal;

import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 04.03.2021
 */
public class ModelFlagpole extends ModelIIBase
{
	int textureX = 256;
	int textureY = 128;

	public ModelFlagpole() //Same as Filename
	{
		baseModel = new ModelRendererTurbo[283];
		baseModel[0] = new ModelRendererTurbo(this, 64, 48, textureX, textureY); // BASE01
		baseModel[1] = new ModelRendererTurbo(this, 208, 57, textureX, textureY); // BASE02
		baseModel[2] = new ModelRendererTurbo(this, 8, 32, textureX, textureY); // BASE03
		baseModel[3] = new ModelRendererTurbo(this, 240, 10, textureX, textureY); // BASE04
		baseModel[4] = new ModelRendererTurbo(this, 8, 32, textureX, textureY); // BASE05
		baseModel[5] = new ModelRendererTurbo(this, 152, 0, textureX, textureY); // BASE06
		baseModel[6] = new ModelRendererTurbo(this, 152, 0, textureX, textureY); // BASE07
		baseModel[7] = new ModelRendererTurbo(this, 2, 97, textureX, textureY); // BASE08
		baseModel[8] = new ModelRendererTurbo(this, 182, 31, textureX, textureY); // BASE10
		baseModel[9] = new ModelRendererTurbo(this, 134, 20, textureX, textureY); // Barrel01
		baseModel[10] = new ModelRendererTurbo(this, 88, 19, textureX, textureY); // Box 14
		baseModel[11] = new ModelRendererTurbo(this, 104, 2, textureX, textureY); // Box 15
		baseModel[12] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Barrel02
		baseModel[13] = new ModelRendererTurbo(this, -2, 96, textureX, textureY); // Box 17
		baseModel[14] = new ModelRendererTurbo(this, 4, 59, textureX, textureY); // Box 18
		baseModel[15] = new ModelRendererTurbo(this, 66, 14, textureX, textureY); // Box 19
		baseModel[16] = new ModelRendererTurbo(this, 76, 62, textureX, textureY); // Ammoboxes01
		baseModel[17] = new ModelRendererTurbo(this, 76, 62, textureX, textureY); // Ammoboxes02
		baseModel[18] = new ModelRendererTurbo(this, 82, 48, textureX, textureY); // Jerrycans01
		baseModel[19] = new ModelRendererTurbo(this, 104, 38, textureX, textureY); // Jerrycans02
		baseModel[20] = new ModelRendererTurbo(this, 208, 79, textureX, textureY); // Jerrycans03
		baseModel[21] = new ModelRendererTurbo(this, 208, 79, textureX, textureY); // Jerrycans04
		baseModel[22] = new ModelRendererTurbo(this, 186, 21, textureX, textureY); // Jerrycans05
		baseModel[23] = new ModelRendererTurbo(this, 82, 48, textureX, textureY); // Jerrycans06
		baseModel[24] = new ModelRendererTurbo(this, 82, 48, textureX, textureY); // Jerrycans07
		baseModel[25] = new ModelRendererTurbo(this, 104, 38, textureX, textureY); // Jerrycans08
		baseModel[26] = new ModelRendererTurbo(this, 208, 79, textureX, textureY); // Jerrycans09
		baseModel[27] = new ModelRendererTurbo(this, 208, 79, textureX, textureY); // Jerrycans10
		baseModel[28] = new ModelRendererTurbo(this, 186, 21, textureX, textureY); // Jerrycans11
		baseModel[29] = new ModelRendererTurbo(this, 104, 38, textureX, textureY); // Jerrycans12
		baseModel[30] = new ModelRendererTurbo(this, 208, 79, textureX, textureY); // Jerrycans13
		baseModel[31] = new ModelRendererTurbo(this, 186, 21, textureX, textureY); // Jerrycans14
		baseModel[32] = new ModelRendererTurbo(this, 208, 79, textureX, textureY); // Jerrycans15
		baseModel[33] = new ModelRendererTurbo(this, 0, 39, textureX, textureY); // Box 38
		baseModel[34] = new ModelRendererTurbo(this, 40, 113, textureX, textureY); // Box 41
		baseModel[35] = new ModelRendererTurbo(this, 0, 0, textureX, textureY); // Box 43
		baseModel[36] = new ModelRendererTurbo(this, 182, 25, textureX, textureY); // Radio01
		baseModel[37] = new ModelRendererTurbo(this, 0, 24, textureX, textureY); // Radio02
		baseModel[38] = new ModelRendererTurbo(this, 0, 24, textureX, textureY); // Radio03
		baseModel[39] = new ModelRendererTurbo(this, 0, 24, textureX, textureY); // Radio04
		baseModel[40] = new ModelRendererTurbo(this, 0, 24, textureX, textureY); // Radio05
		baseModel[41] = new ModelRendererTurbo(this, 66, 2, textureX, textureY); // Box 49
		baseModel[42] = new ModelRendererTurbo(this, 66, 2, textureX, textureY); // Box 50
		baseModel[43] = new ModelRendererTurbo(this, 210, 79, textureX, textureY); // Radio06
		baseModel[44] = new ModelRendererTurbo(this, 120, 19, textureX, textureY); // Radio23
		baseModel[45] = new ModelRendererTurbo(this, 140, 5, textureX, textureY); // Radio24
		baseModel[46] = new ModelRendererTurbo(this, 72, 53, textureX, textureY); // Radio25
		baseModel[47] = new ModelRendererTurbo(this, 76, 53, textureX, textureY); // Box 71
		baseModel[48] = new ModelRendererTurbo(this, 76, 53, textureX, textureY); // Box 72
		baseModel[49] = new ModelRendererTurbo(this, 76, 53, textureX, textureY); // Box 73
		baseModel[50] = new ModelRendererTurbo(this, 76, 53, textureX, textureY); // Box 74
		baseModel[51] = new ModelRendererTurbo(this, 110, 46, textureX, textureY); // Box 75
		baseModel[52] = new ModelRendererTurbo(this, 64, 61, textureX, textureY); // Box 76
		baseModel[53] = new ModelRendererTurbo(this, 64, 61, textureX, textureY); // Box 77
		baseModel[54] = new ModelRendererTurbo(this, 208, 43, textureX, textureY); // Box 78
		baseModel[55] = new ModelRendererTurbo(this, 144, 10, textureX, textureY); // Box 79
		baseModel[56] = new ModelRendererTurbo(this, 144, 10, textureX, textureY); // Box 80
		baseModel[57] = new ModelRendererTurbo(this, 140, 10, textureX, textureY); // Box 81
		baseModel[58] = new ModelRendererTurbo(this, 148, 6, textureX, textureY); // Box 82
		baseModel[59] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // Box 83
		baseModel[60] = new ModelRendererTurbo(this, 170, 21, textureX, textureY); // Box 84
		baseModel[61] = new ModelRendererTurbo(this, 140, 1, textureX, textureY); // Box 85
		baseModel[62] = new ModelRendererTurbo(this, 8, 4, textureX, textureY); // Box 86
		baseModel[63] = new ModelRendererTurbo(this, 8, 0, textureX, textureY); // Box 87
		baseModel[64] = new ModelRendererTurbo(this, 148, 2, textureX, textureY); // Box 88
		baseModel[65] = new ModelRendererTurbo(this, 176, 22, textureX, textureY); // Box 89
		baseModel[66] = new ModelRendererTurbo(this, 144, 10, textureX, textureY); // Box 90
		baseModel[67] = new ModelRendererTurbo(this, 140, 10, textureX, textureY); // Box 91
		baseModel[68] = new ModelRendererTurbo(this, 148, 6, textureX, textureY); // Box 92
		baseModel[69] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // Box 93
		baseModel[70] = new ModelRendererTurbo(this, 170, 21, textureX, textureY); // Box 94
		baseModel[71] = new ModelRendererTurbo(this, 140, 1, textureX, textureY); // Box 95
		baseModel[72] = new ModelRendererTurbo(this, 8, 4, textureX, textureY); // Box 96
		baseModel[73] = new ModelRendererTurbo(this, 8, 0, textureX, textureY); // Box 97
		baseModel[74] = new ModelRendererTurbo(this, 148, 2, textureX, textureY); // Box 98
		baseModel[75] = new ModelRendererTurbo(this, 176, 22, textureX, textureY); // Box 99
		baseModel[76] = new ModelRendererTurbo(this, 144, 10, textureX, textureY); // Box 100
		baseModel[77] = new ModelRendererTurbo(this, 140, 10, textureX, textureY); // Box 101
		baseModel[78] = new ModelRendererTurbo(this, 148, 6, textureX, textureY); // Box 102
		baseModel[79] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // Box 103
		baseModel[80] = new ModelRendererTurbo(this, 170, 21, textureX, textureY); // Box 104
		baseModel[81] = new ModelRendererTurbo(this, 140, 1, textureX, textureY); // Box 105
		baseModel[82] = new ModelRendererTurbo(this, 8, 4, textureX, textureY); // Box 106
		baseModel[83] = new ModelRendererTurbo(this, 8, 0, textureX, textureY); // Box 107
		baseModel[84] = new ModelRendererTurbo(this, 148, 2, textureX, textureY); // Box 108
		baseModel[85] = new ModelRendererTurbo(this, 176, 22, textureX, textureY); // Box 109
		baseModel[86] = new ModelRendererTurbo(this, 144, 10, textureX, textureY); // Box 110
		baseModel[87] = new ModelRendererTurbo(this, 140, 10, textureX, textureY); // Box 111
		baseModel[88] = new ModelRendererTurbo(this, 148, 6, textureX, textureY); // Box 112
		baseModel[89] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // Box 113
		baseModel[90] = new ModelRendererTurbo(this, 170, 21, textureX, textureY); // Box 114
		baseModel[91] = new ModelRendererTurbo(this, 140, 1, textureX, textureY); // Box 115
		baseModel[92] = new ModelRendererTurbo(this, 8, 4, textureX, textureY); // Box 116
		baseModel[93] = new ModelRendererTurbo(this, 8, 0, textureX, textureY); // Box 117
		baseModel[94] = new ModelRendererTurbo(this, 148, 2, textureX, textureY); // Box 118
		baseModel[95] = new ModelRendererTurbo(this, 176, 22, textureX, textureY); // Box 119
		baseModel[96] = new ModelRendererTurbo(this, 144, 10, textureX, textureY); // Box 120
		baseModel[97] = new ModelRendererTurbo(this, 140, 10, textureX, textureY); // Box 121
		baseModel[98] = new ModelRendererTurbo(this, 148, 6, textureX, textureY); // Box 122
		baseModel[99] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // Box 123
		baseModel[100] = new ModelRendererTurbo(this, 170, 21, textureX, textureY); // Box 124
		baseModel[101] = new ModelRendererTurbo(this, 140, 1, textureX, textureY); // Box 125
		baseModel[102] = new ModelRendererTurbo(this, 8, 4, textureX, textureY); // Box 126
		baseModel[103] = new ModelRendererTurbo(this, 8, 0, textureX, textureY); // Box 127
		baseModel[104] = new ModelRendererTurbo(this, 148, 2, textureX, textureY); // Box 128
		baseModel[105] = new ModelRendererTurbo(this, 176, 22, textureX, textureY); // Box 129
		baseModel[106] = new ModelRendererTurbo(this, 144, 10, textureX, textureY); // Box 130
		baseModel[107] = new ModelRendererTurbo(this, 140, 10, textureX, textureY); // Box 131
		baseModel[108] = new ModelRendererTurbo(this, 148, 6, textureX, textureY); // Box 132
		baseModel[109] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // Box 133
		baseModel[110] = new ModelRendererTurbo(this, 170, 21, textureX, textureY); // Box 134
		baseModel[111] = new ModelRendererTurbo(this, 140, 1, textureX, textureY); // Box 135
		baseModel[112] = new ModelRendererTurbo(this, 8, 4, textureX, textureY); // Box 136
		baseModel[113] = new ModelRendererTurbo(this, 8, 0, textureX, textureY); // Box 137
		baseModel[114] = new ModelRendererTurbo(this, 148, 2, textureX, textureY); // Box 138
		baseModel[115] = new ModelRendererTurbo(this, 176, 22, textureX, textureY); // Box 139
		baseModel[116] = new ModelRendererTurbo(this, 144, 10, textureX, textureY); // Box 140
		baseModel[117] = new ModelRendererTurbo(this, 140, 10, textureX, textureY); // Box 141
		baseModel[118] = new ModelRendererTurbo(this, 148, 6, textureX, textureY); // Box 142
		baseModel[119] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // Box 143
		baseModel[120] = new ModelRendererTurbo(this, 170, 21, textureX, textureY); // Box 144
		baseModel[121] = new ModelRendererTurbo(this, 140, 1, textureX, textureY); // Box 145
		baseModel[122] = new ModelRendererTurbo(this, 8, 4, textureX, textureY); // Box 146
		baseModel[123] = new ModelRendererTurbo(this, 8, 0, textureX, textureY); // Box 147
		baseModel[124] = new ModelRendererTurbo(this, 148, 2, textureX, textureY); // Box 148
		baseModel[125] = new ModelRendererTurbo(this, 176, 22, textureX, textureY); // Box 149
		baseModel[126] = new ModelRendererTurbo(this, 144, 10, textureX, textureY); // Box 150
		baseModel[127] = new ModelRendererTurbo(this, 140, 10, textureX, textureY); // Box 151
		baseModel[128] = new ModelRendererTurbo(this, 148, 6, textureX, textureY); // Box 152
		baseModel[129] = new ModelRendererTurbo(this, 8, 8, textureX, textureY); // Box 153
		baseModel[130] = new ModelRendererTurbo(this, 170, 21, textureX, textureY); // Box 154
		baseModel[131] = new ModelRendererTurbo(this, 140, 1, textureX, textureY); // Box 155
		baseModel[132] = new ModelRendererTurbo(this, 8, 4, textureX, textureY); // Box 156
		baseModel[133] = new ModelRendererTurbo(this, 8, 0, textureX, textureY); // Box 157
		baseModel[134] = new ModelRendererTurbo(this, 148, 2, textureX, textureY); // Box 158
		baseModel[135] = new ModelRendererTurbo(this, 40, 97, textureX, textureY); // Box 160
		baseModel[136] = new ModelRendererTurbo(this, 40, 105, textureX, textureY); // Box 166
		baseModel[137] = new ModelRendererTurbo(this, 40, 97, textureX, textureY); // Box 167
		baseModel[138] = new ModelRendererTurbo(this, 40, 105, textureX, textureY); // Box 168
		baseModel[139] = new ModelRendererTurbo(this, 16, 39, textureX, textureY); // Box 169
		baseModel[140] = new ModelRendererTurbo(this, 24, 24, textureX, textureY); // Box 170
		baseModel[141] = new ModelRendererTurbo(this, 64, 45, textureX, textureY); // Box 171
		baseModel[142] = new ModelRendererTurbo(this, 70, 31, textureX, textureY); // Box 172
		baseModel[143] = new ModelRendererTurbo(this, 64, 45, textureX, textureY); // Box 173
		baseModel[144] = new ModelRendererTurbo(this, 32, 68, textureX, textureY); // Box 174
		baseModel[145] = new ModelRendererTurbo(this, 16, 39, textureX, textureY); // Box 175
		baseModel[146] = new ModelRendererTurbo(this, 32, 68, textureX, textureY); // Box 176
		baseModel[147] = new ModelRendererTurbo(this, 4, 105, textureX, textureY); // Box 178
		baseModel[148] = new ModelRendererTurbo(this, 4, 105, textureX, textureY); // Box 179
		baseModel[149] = new ModelRendererTurbo(this, 4, 27, textureX, textureY); // Box 180
		baseModel[150] = new ModelRendererTurbo(this, 4, 27, textureX, textureY); // Box 181
		baseModel[151] = new ModelRendererTurbo(this, 200, 38, textureX, textureY); // Box 184
		baseModel[152] = new ModelRendererTurbo(this, 4, 105, textureX, textureY); // Box 185
		baseModel[153] = new ModelRendererTurbo(this, 4, 105, textureX, textureY); // Box 186
		baseModel[154] = new ModelRendererTurbo(this, 4, 27, textureX, textureY); // Box 187
		baseModel[155] = new ModelRendererTurbo(this, 82, 5, textureX, textureY); // Box 188
		baseModel[156] = new ModelRendererTurbo(this, 82, 0, textureX, textureY); // Box 189
		baseModel[157] = new ModelRendererTurbo(this, 82, 5, textureX, textureY); // Box 190
		baseModel[158] = new ModelRendererTurbo(this, 182, 41, textureX, textureY); // Box 191
		baseModel[159] = new ModelRendererTurbo(this, 38, 60, textureX, textureY); // Box 192
		baseModel[160] = new ModelRendererTurbo(this, 38, 60, textureX, textureY); // Box 193
		baseModel[161] = new ModelRendererTurbo(this, 182, 41, textureX, textureY); // Box 194
		baseModel[162] = new ModelRendererTurbo(this, 38, 60, textureX, textureY); // Box 195
		baseModel[163] = new ModelRendererTurbo(this, 82, 0, textureX, textureY); // Box 196
		baseModel[164] = new ModelRendererTurbo(this, 70, 39, textureX, textureY); // Box 197
		baseModel[165] = new ModelRendererTurbo(this, 182, 41, textureX, textureY); // Box 198
		baseModel[166] = new ModelRendererTurbo(this, 38, 60, textureX, textureY); // Box 199
		baseModel[167] = new ModelRendererTurbo(this, 38, 60, textureX, textureY); // Box 200
		baseModel[168] = new ModelRendererTurbo(this, 96, 10, textureX, textureY); // Radio23
		baseModel[169] = new ModelRendererTurbo(this, 40, 51, textureX, textureY); // Radio23
		baseModel[170] = new ModelRendererTurbo(this, 46, 0, textureX, textureY); // Radio23
		baseModel[171] = new ModelRendererTurbo(this, 90, 44, textureX, textureY); // Radio23
		baseModel[172] = new ModelRendererTurbo(this, 58, 9, textureX, textureY); // Radio23
		baseModel[173] = new ModelRendererTurbo(this, 82, 44, textureX, textureY); // Radio23
		baseModel[174] = new ModelRendererTurbo(this, 62, 4, textureX, textureY); // Radio23
		baseModel[175] = new ModelRendererTurbo(this, 54, 4, textureX, textureY); // Radio23
		baseModel[176] = new ModelRendererTurbo(this, 54, 0, textureX, textureY); // Radio23
		baseModel[177] = new ModelRendererTurbo(this, 14, 28, textureX, textureY); // Radio23
		baseModel[178] = new ModelRendererTurbo(this, 98, 44, textureX, textureY); // Radio23
		baseModel[179] = new ModelRendererTurbo(this, 22, 24, textureX, textureY); // Radio23
		baseModel[180] = new ModelRendererTurbo(this, 64, 53, textureX, textureY); // Radio23
		baseModel[181] = new ModelRendererTurbo(this, 48, 28, textureX, textureY); // Radio23
		baseModel[182] = new ModelRendererTurbo(this, 4, 24, textureX, textureY); // Radio23
		baseModel[183] = new ModelRendererTurbo(this, 48, 51, textureX, textureY); // Radio23
		baseModel[184] = new ModelRendererTurbo(this, 40, 47, textureX, textureY); // Radio23
		baseModel[185] = new ModelRendererTurbo(this, 64, 31, textureX, textureY); // Radio23
		baseModel[186] = new ModelRendererTurbo(this, 4, 28, textureX, textureY); // Radio23
		baseModel[187] = new ModelRendererTurbo(this, 58, 20, textureX, textureY); // Radio23
		baseModel[188] = new ModelRendererTurbo(this, 40, 24, textureX, textureY); // Radio23
		baseModel[189] = new ModelRendererTurbo(this, 40, 39, textureX, textureY); // Radio23
		baseModel[190] = new ModelRendererTurbo(this, 22, 28, textureX, textureY); // Radio23
		baseModel[191] = new ModelRendererTurbo(this, 240, 2, textureX, textureY); // Radio23
		baseModel[192] = new ModelRendererTurbo(this, 4, 24, textureX, textureY); // Radio23
		baseModel[193] = new ModelRendererTurbo(this, 64, 31, textureX, textureY); // Radio23
		baseModel[194] = new ModelRendererTurbo(this, 58, 13, textureX, textureY); // Radio23
		baseModel[195] = new ModelRendererTurbo(this, 110, 10, textureX, textureY); // Radio23
		baseModel[196] = new ModelRendererTurbo(this, 202, 20, textureX, textureY); // Box 184
		baseModel[197] = new ModelRendererTurbo(this, 200, 38, textureX, textureY); // Box 184
		baseModel[198] = new ModelRendererTurbo(this, 202, 20, textureX, textureY); // Box 184
		baseModel[199] = new ModelRendererTurbo(this, 120, 29, textureX, textureY); // Jerrycans12
		baseModel[200] = new ModelRendererTurbo(this, 120, 29, textureX, textureY); // Jerrycans12
		baseModel[201] = new ModelRendererTurbo(this, 120, 29, textureX, textureY); // Jerrycans12
		baseModel[202] = new ModelRendererTurbo(this, 214, 17, textureX, textureY); // Box 149
		baseModel[203] = new ModelRendererTurbo(this, 214, 17, textureX, textureY); // Box 149
		baseModel[204] = new ModelRendererTurbo(this, 214, 17, textureX, textureY); // Box 149
		baseModel[205] = new ModelRendererTurbo(this, 214, 17, textureX, textureY); // Box 149
		baseModel[206] = new ModelRendererTurbo(this, 214, 17, textureX, textureY); // Box 149
		baseModel[207] = new ModelRendererTurbo(this, 214, 17, textureX, textureY); // Box 149
		baseModel[208] = new ModelRendererTurbo(this, 214, 17, textureX, textureY); // Box 149
		baseModel[209] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 88
		baseModel[210] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 87
		baseModel[211] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 86
		baseModel[212] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 85
		baseModel[213] = new ModelRendererTurbo(this, 217, 20, textureX, textureY); // Box 84
		baseModel[214] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 83
		baseModel[215] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 82
		baseModel[216] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 81
		baseModel[217] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 80
		baseModel[218] = new ModelRendererTurbo(this, 214, 17, textureX, textureY); // Box 149
		baseModel[219] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 88
		baseModel[220] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 87
		baseModel[221] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 86
		baseModel[222] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 85
		baseModel[223] = new ModelRendererTurbo(this, 217, 20, textureX, textureY); // Box 84
		baseModel[224] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 83
		baseModel[225] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 82
		baseModel[226] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 81
		baseModel[227] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 80
		baseModel[228] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 88
		baseModel[229] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 87
		baseModel[230] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 86
		baseModel[231] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 85
		baseModel[232] = new ModelRendererTurbo(this, 217, 20, textureX, textureY); // Box 84
		baseModel[233] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 83
		baseModel[234] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 82
		baseModel[235] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 81
		baseModel[236] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 80
		baseModel[237] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 88
		baseModel[238] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 87
		baseModel[239] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 86
		baseModel[240] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 85
		baseModel[241] = new ModelRendererTurbo(this, 217, 20, textureX, textureY); // Box 84
		baseModel[242] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 83
		baseModel[243] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 82
		baseModel[244] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 81
		baseModel[245] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 80
		baseModel[246] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 88
		baseModel[247] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 87
		baseModel[248] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 86
		baseModel[249] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 85
		baseModel[250] = new ModelRendererTurbo(this, 217, 20, textureX, textureY); // Box 84
		baseModel[251] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 83
		baseModel[252] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 82
		baseModel[253] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 81
		baseModel[254] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 80
		baseModel[255] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 88
		baseModel[256] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 87
		baseModel[257] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 86
		baseModel[258] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 85
		baseModel[259] = new ModelRendererTurbo(this, 217, 20, textureX, textureY); // Box 84
		baseModel[260] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 83
		baseModel[261] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 82
		baseModel[262] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 81
		baseModel[263] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 80
		baseModel[264] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 88
		baseModel[265] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 87
		baseModel[266] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 86
		baseModel[267] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 85
		baseModel[268] = new ModelRendererTurbo(this, 217, 20, textureX, textureY); // Box 84
		baseModel[269] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 83
		baseModel[270] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 82
		baseModel[271] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 81
		baseModel[272] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 80
		baseModel[273] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 88
		baseModel[274] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 87
		baseModel[275] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 86
		baseModel[276] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 85
		baseModel[277] = new ModelRendererTurbo(this, 217, 20, textureX, textureY); // Box 84
		baseModel[278] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 83
		baseModel[279] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 82
		baseModel[280] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 81
		baseModel[281] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 80
		baseModel[282] = new ModelRendererTurbo(this, 214, 25, textureX, textureY); // Box 79

		baseModel[0].addBox(0F, 0F, 0F, 48, 32, 48, 0F); // BASE01
		baseModel[0].setRotationPoint(0F, -8F, 0F);

		baseModel[1].addBox(0F, 0F, 0F, 8, 14, 8, 0F); // BASE02
		baseModel[1].setRotationPoint(20F, -22F, 20F);

		baseModel[2].addBox(0F, 0F, 0F, 4, 16, 48, 0F); // BASE03
		baseModel[2].setRotationPoint(0F, -24F, 0F);

		baseModel[3].addBox(0F, 0F, 0F, 4, 82, 4, 0F); // BASE04
		baseModel[3].setRotationPoint(22F, -104F, 22F);

		baseModel[4].addBox(0F, 0F, 0F, 4, 16, 48, 0F); // BASE05
		baseModel[4].setRotationPoint(44F, -24F, 0F);

		baseModel[5].addBox(0F, 0F, 0F, 40, 16, 4, 0F); // BASE06
		baseModel[5].setRotationPoint(4F, -24F, 0F);

		baseModel[6].addBox(0F, 0F, 0F, 40, 16, 4, 0F); // BASE07
		baseModel[6].setRotationPoint(4F, -24F, 44F);

		baseModel[7].addBox(0F, 0F, 0F, 5, 3, 5, 0F); // BASE08
		baseModel[7].setRotationPoint(21.5F, -107F, 21.5F);

		baseModel[8].addBox(0F, 0F, 0F, 3, 4, 2, 0F); // BASE10
		baseModel[8].setRotationPoint(26F, -29F, 23F);

		baseModel[9].addBox(0F, 0F, 0F, 12, 14, 12, 0F); // Barrel01
		baseModel[9].setRotationPoint(31.5F, -22F, 4F);
		baseModel[9].rotateAngleY = 0.10471976F;

		baseModel[10].addBox(0F, 0F, 0F, 9, 5, 14, 0F); // Box 14
		baseModel[10].setRotationPoint(4F, -13F, 29F);

		baseModel[11].addBox(0F, 0F, 0F, 12, 5, 12, 0F); // Box 15
		baseModel[11].setRotationPoint(5F, -13F, 16.5F);
		baseModel[11].rotateAngleY = 0.03490659F;

		baseModel[12].addBox(0F, 0F, 0F, 17, 12, 12, 0F); // Barrel02
		baseModel[12].setRotationPoint(4F, -20F, 4F);

		baseModel[13].addBox(0F, 0F, 0F, 9, 8, 24, 0F); // Box 17
		baseModel[13].setRotationPoint(5F, -21F, 16.5F);
		baseModel[13].rotateAngleY = -0.01745329F;

		baseModel[14].addBox(0F, 0F, 0F, 9, 8, 9, 0F); // Box 18
		baseModel[14].setRotationPoint(21F, -16F, 4F);

		baseModel[15].addBox(0F, 0F, 0F, 6, 5, 12, 0F); // Box 19
		baseModel[15].setRotationPoint(8F, -26F, 25F);
		baseModel[15].rotateAngleY = 0.27925268F;

		baseModel[16].addBox(0F, 0F, 0F, 12, 12, 6, 0F); // Ammoboxes01
		baseModel[16].setRotationPoint(32F, -20F, 18F);

		baseModel[17].addBox(0F, 0F, 0F, 12, 12, 6, 0F); // Ammoboxes02
		baseModel[17].setRotationPoint(32F, -20F, 25F);

		baseModel[18].addBox(0F, 0F, 0F, 11, 10, 4, 0F); // Jerrycans01
		baseModel[18].setRotationPoint(22F, -18F, 40F);

		baseModel[19].addShapeBox(0F, 0F, 0F, 11, 4, 4, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Jerrycans02
		baseModel[19].setRotationPoint(22F, -22F, 40F);

		baseModel[20].addBox(0F, 0F, 0F, 1, 1, 2, 0F); // Jerrycans03
		baseModel[20].setRotationPoint(26F, -23F, 41F);

		baseModel[21].addBox(0F, 0F, 0F, 1, 1, 2, 0F); // Jerrycans04
		baseModel[21].setRotationPoint(31F, -23F, 41F);

		baseModel[22].addShapeBox(0F, 0F, 0F, 6, 1, 2, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Jerrycans05
		baseModel[22].setRotationPoint(26F, -24F, 41F);

		baseModel[23].addBox(0F, 0F, 0F, 11, 10, 4, 0F); // Jerrycans06
		baseModel[23].setRotationPoint(22F, -18F, 35.5F);

		baseModel[24].addBox(0F, 0F, 0F, 11, 10, 4, 0F); // Jerrycans07
		baseModel[24].setRotationPoint(19F, -18F, 32.2F);
		baseModel[24].rotateAngleY = 1.25663706F;

		baseModel[25].addShapeBox(0F, 0F, 0F, 11, 4, 4, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Jerrycans08
		baseModel[25].setRotationPoint(22F, -22F, 35.5F);

		baseModel[26].addBox(0F, 0F, 0F, 1, 1, 2, 0F); // Jerrycans09
		baseModel[26].setRotationPoint(26F, -23F, 36.5F);

		baseModel[27].addBox(0F, 0F, 0F, 1, 1, 2, 0F); // Jerrycans10
		baseModel[27].setRotationPoint(31F, -23F, 36.5F);

		baseModel[28].addShapeBox(0F, 0F, 0F, 6, 1, 2, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Jerrycans11
		baseModel[28].setRotationPoint(26F, -24F, 36.5F);

		baseModel[29].addShapeBox(0F, 0F, 0F, 11, 4, 4, 0F, 0F, 0F, 0F, -3F, 0F, 0F, -3F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Jerrycans12
		baseModel[29].setRotationPoint(19F, -22F, 32.2F);
		baseModel[29].rotateAngleY = 1.25663706F;

		baseModel[30].addBox(6F, 0F, 1F, 1, 1, 2, 0F); // Jerrycans13
		baseModel[30].setRotationPoint(19F, -23F, 32.2F);
		baseModel[30].rotateAngleY = 1.25663706F;

		baseModel[31].addShapeBox(1F, 0F, 1F, 6, 1, 2, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Jerrycans14
		baseModel[31].setRotationPoint(19F, -24F, 32.2F);
		baseModel[31].rotateAngleY = 1.25663706F;

		baseModel[32].addBox(1F, 0F, 1F, 1, 1, 2, 0F); // Jerrycans15
		baseModel[32].setRotationPoint(19F, -23F, 32.2F);
		baseModel[32].rotateAngleY = 1.25663706F;

		baseModel[33].addBox(0F, 0F, 0F, 13, 13, 3, 0F); // Box 38
		baseModel[33].setRotationPoint(17F, -21F, 28F);
		baseModel[33].rotateAngleX = 0.05235988F;

		baseModel[34].addBox(0F, 0F, 0F, 10, 2, 2, 0F); // Box 41
		baseModel[34].setRotationPoint(4F, -22F, 6F);
		baseModel[34].rotateAngleY = -0.20943951F;

		baseModel[35].addBox(0F, 0F, 0F, 2, 10, 2, 0F); // Box 43
		baseModel[35].setRotationPoint(33F, -18F, 31.5F);
		baseModel[35].rotateAngleY = -0.17453293F;
		baseModel[35].rotateAngleZ = -0.12217305F;

		baseModel[36].addBox(0F, 0F, 0F, 10, 1, 12, 0F); // Radio01
		baseModel[36].setRotationPoint(34F, -21F, 31.5F);

		baseModel[37].addBox(0F, 0F, 0F, 1, 12, 1, 0F); // Radio02
		baseModel[37].setRotationPoint(35F, -20F, 32.5F);

		baseModel[38].addBox(0F, 0F, 0F, 1, 12, 1, 0F); // Radio03
		baseModel[38].setRotationPoint(42F, -20F, 32.5F);

		baseModel[39].addBox(0F, 0F, 0F, 1, 12, 1, 0F); // Radio04
		baseModel[39].setRotationPoint(42F, -20F, 41.5F);

		baseModel[40].addBox(0F, 0F, 0F, 1, 12, 1, 0F); // Radio05
		baseModel[40].setRotationPoint(35F, -20F, 41.5F);

		baseModel[41].addBox(0F, 0F, 0F, 4, 3, 8, 0F); // Box 49
		baseModel[41].setRotationPoint(38F, -11F, 32F);
		baseModel[41].rotateAngleY = 0.19198622F;

		baseModel[42].addBox(0F, 0F, 0F, 4, 3, 8, 0F); // Box 50
		baseModel[42].setRotationPoint(37F, -14F, 32F);
		baseModel[42].rotateAngleY = -0.12217305F;

		baseModel[43].addBox(0F, 0F, 0F, 5, 7, 10, 0F); // Radio06
		baseModel[43].setRotationPoint(38F, -28F, 31F);
		baseModel[43].rotateAngleY = 0.12217305F;

		baseModel[44].addBox(0F, 0F, 0F, 7, 4, 6, 0F); // Radio23
		baseModel[44].setRotationPoint(6F, -25F, 16F);

		baseModel[45].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Radio24
		baseModel[45].setRotationPoint(7F, -28F, 19F);

		baseModel[46].addBox(0F, 0F, 0F, 1, 22, 1, 0F); // Radio25
		baseModel[46].setRotationPoint(7.5F, -50F, 19.5F);

		baseModel[47].addBox(0F, 0F, 0F, 1, 13, 2, 0F); // Box 71
		baseModel[47].setRotationPoint(0.99F, -34F, -1F);
		baseModel[47].rotateAngleX = 0.57595865F;

		baseModel[48].addBox(0F, 0F, 0F, 1, 13, 2, 0F); // Box 72
		baseModel[48].setRotationPoint(46F, -34F, -1F);
		baseModel[48].rotateAngleX = 0.57595865F;

		baseModel[49].addBox(0F, 0F, 0F, 1, 13, 2, 0F); // Box 73
		baseModel[49].setRotationPoint(46F, -35F, 6F);
		baseModel[49].rotateAngleX = -0.57595865F;

		baseModel[50].addBox(0F, 0F, 0F, 1, 13, 2, 0F); // Box 74
		baseModel[50].setRotationPoint(1F, -35F, 6F);
		baseModel[50].rotateAngleX = -0.57595865F;

		baseModel[51].addBox(0F, 0F, 0F, 48, 1, 1, 0F); // Box 75
		baseModel[51].setRotationPoint(0F, -32F, 3F);

		baseModel[52].addBox(0F, 0F, 0F, 2, 14, 2, 0F); // Box 76
		baseModel[52].setRotationPoint(45F, -38F, 30.5F);
		baseModel[52].rotateAngleY = -1.25663706F;

		baseModel[53].addBox(-17F, 0F, 0F, 2, 14, 2, 0F); // Box 77
		baseModel[53].setRotationPoint(45F, -38F, 30.5F);
		baseModel[53].rotateAngleY = -1.25663706F;

		baseModel[54].addBox(-16F, 0F, 0F, 16, 14, 0, 0F); // Box 78
		baseModel[54].setRotationPoint(46F, -38F, 30.7F);
		baseModel[54].rotateAngleY = -1.25663706F;

		baseModel[55].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 79
		baseModel[55].setRotationPoint(1.5F, -33F, 5F);

		baseModel[56].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 80
		baseModel[56].setRotationPoint(1.5F, -33F, 8F);
		baseModel[56].rotateAngleX = -0.89011792F;
		baseModel[56].rotateAngleZ = 0.54105207F;

		baseModel[57].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 81
		baseModel[57].setRotationPoint(2.8F, -31.2F, 9F);
		baseModel[57].rotateAngleZ = 0.10471976F;

		baseModel[58].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 82
		baseModel[58].setRotationPoint(3.2F, -29F, 9F);
		baseModel[58].rotateAngleX = -0.59341195F;
		baseModel[58].rotateAngleZ = 0.10471976F;

		baseModel[59].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 83
		baseModel[59].setRotationPoint(3.8F, -27F, 8F);
		baseModel[59].rotateAngleX = -1.01229097F;
		baseModel[59].rotateAngleZ = 0.10471976F;

		baseModel[60].addBox(0F, 0F, 0F, 1, 1, 5, 0F); // Box 84
		baseModel[60].setRotationPoint(4.8F, -25.4F, 1F);

		baseModel[61].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 85
		baseModel[61].setRotationPoint(5.2F, -26F, -1F);
		baseModel[61].rotateAngleX = 1.01229097F;
		baseModel[61].rotateAngleZ = -0.10471976F;

		baseModel[62].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 86
		baseModel[62].setRotationPoint(5.8F, -28F, -2.5F);
		baseModel[62].rotateAngleX = 0.59341195F;
		baseModel[62].rotateAngleZ = -0.10471976F;

		baseModel[63].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 87
		baseModel[63].setRotationPoint(6.3F, -31F, -2.5F);
		baseModel[63].rotateAngleZ = -0.10471976F;

		baseModel[64].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 88
		baseModel[64].setRotationPoint(6.8F, -33F, -0.5F);
		baseModel[64].rotateAngleX = -0.80285146F;
		baseModel[64].rotateAngleZ = -0.10471976F;

		baseModel[65].addBox(0F, 0F, 0F, 1, 1, 8, 0F); // Box 89
		baseModel[65].setRotationPoint(7.2F, -33.2F, -0.5F);

		baseModel[66].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 90
		baseModel[66].setRotationPoint(7.5F, -33F, 8F);
		baseModel[66].rotateAngleX = -0.89011792F;
		baseModel[66].rotateAngleZ = 0.54105207F;

		baseModel[67].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 91
		baseModel[67].setRotationPoint(8.8F, -31.2F, 9F);
		baseModel[67].rotateAngleZ = 0.10471976F;

		baseModel[68].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 92
		baseModel[68].setRotationPoint(9.2F, -29F, 9F);
		baseModel[68].rotateAngleX = -0.59341195F;
		baseModel[68].rotateAngleZ = 0.10471976F;

		baseModel[69].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 93
		baseModel[69].setRotationPoint(9.8F, -27F, 8F);
		baseModel[69].rotateAngleX = -1.01229097F;
		baseModel[69].rotateAngleZ = 0.10471976F;

		baseModel[70].addBox(0F, 0F, 0F, 1, 1, 5, 0F); // Box 94
		baseModel[70].setRotationPoint(10.8F, -25.4F, 1F);

		baseModel[71].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 95
		baseModel[71].setRotationPoint(11.2F, -26F, -1F);
		baseModel[71].rotateAngleX = 1.01229097F;
		baseModel[71].rotateAngleZ = -0.10471976F;

		baseModel[72].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 96
		baseModel[72].setRotationPoint(11.8F, -28F, -2.5F);
		baseModel[72].rotateAngleX = 0.59341195F;
		baseModel[72].rotateAngleZ = -0.10471976F;

		baseModel[73].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 97
		baseModel[73].setRotationPoint(12.3F, -31F, -2.5F);
		baseModel[73].rotateAngleZ = -0.10471976F;

		baseModel[74].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 98
		baseModel[74].setRotationPoint(12.8F, -33F, -0.5F);
		baseModel[74].rotateAngleX = -0.80285146F;
		baseModel[74].rotateAngleZ = -0.10471976F;

		baseModel[75].addBox(0F, 0F, 0F, 1, 1, 8, 0F); // Box 99
		baseModel[75].setRotationPoint(13.2F, -33.2F, -0.5F);

		baseModel[76].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 100
		baseModel[76].setRotationPoint(13.5F, -33F, 8F);
		baseModel[76].rotateAngleX = -0.89011792F;
		baseModel[76].rotateAngleZ = 0.54105207F;

		baseModel[77].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 101
		baseModel[77].setRotationPoint(14.8F, -31.2F, 9F);
		baseModel[77].rotateAngleZ = 0.10471976F;

		baseModel[78].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 102
		baseModel[78].setRotationPoint(15.2F, -29F, 9F);
		baseModel[78].rotateAngleX = -0.59341195F;
		baseModel[78].rotateAngleZ = 0.10471976F;

		baseModel[79].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 103
		baseModel[79].setRotationPoint(15.8F, -27F, 8F);
		baseModel[79].rotateAngleX = -1.01229097F;
		baseModel[79].rotateAngleZ = 0.10471976F;

		baseModel[80].addBox(0F, 0F, 0F, 1, 1, 5, 0F); // Box 104
		baseModel[80].setRotationPoint(16.8F, -25.4F, 1F);

		baseModel[81].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 105
		baseModel[81].setRotationPoint(17.2F, -26F, -1F);
		baseModel[81].rotateAngleX = 1.01229097F;
		baseModel[81].rotateAngleZ = -0.10471976F;

		baseModel[82].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 106
		baseModel[82].setRotationPoint(17.8F, -28F, -2.5F);
		baseModel[82].rotateAngleX = 0.59341195F;
		baseModel[82].rotateAngleZ = -0.10471976F;

		baseModel[83].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 107
		baseModel[83].setRotationPoint(18.3F, -31F, -2.5F);
		baseModel[83].rotateAngleZ = -0.10471976F;

		baseModel[84].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 108
		baseModel[84].setRotationPoint(18.8F, -33F, -0.5F);
		baseModel[84].rotateAngleX = -0.80285146F;
		baseModel[84].rotateAngleZ = -0.10471976F;

		baseModel[85].addBox(0F, 0F, 0F, 1, 1, 8, 0F); // Box 109
		baseModel[85].setRotationPoint(19.2F, -33.2F, -0.5F);

		baseModel[86].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 110
		baseModel[86].setRotationPoint(19.5F, -33F, 8F);
		baseModel[86].rotateAngleX = -0.89011792F;
		baseModel[86].rotateAngleZ = 0.54105207F;

		baseModel[87].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 111
		baseModel[87].setRotationPoint(20.8F, -31.2F, 9F);
		baseModel[87].rotateAngleZ = 0.10471976F;

		baseModel[88].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 112
		baseModel[88].setRotationPoint(21.2F, -29F, 9F);
		baseModel[88].rotateAngleX = -0.59341195F;
		baseModel[88].rotateAngleZ = 0.10471976F;

		baseModel[89].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 113
		baseModel[89].setRotationPoint(21.8F, -27F, 8F);
		baseModel[89].rotateAngleX = -1.01229097F;
		baseModel[89].rotateAngleZ = 0.10471976F;

		baseModel[90].addBox(0F, 0F, 0F, 1, 1, 5, 0F); // Box 114
		baseModel[90].setRotationPoint(22.8F, -25.4F, 1F);

		baseModel[91].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 115
		baseModel[91].setRotationPoint(23.2F, -26F, -1F);
		baseModel[91].rotateAngleX = 1.01229097F;
		baseModel[91].rotateAngleZ = -0.10471976F;

		baseModel[92].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 116
		baseModel[92].setRotationPoint(23.8F, -28F, -2.5F);
		baseModel[92].rotateAngleX = 0.59341195F;
		baseModel[92].rotateAngleZ = -0.10471976F;

		baseModel[93].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 117
		baseModel[93].setRotationPoint(24.3F, -31F, -2.5F);
		baseModel[93].rotateAngleZ = -0.10471976F;

		baseModel[94].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 118
		baseModel[94].setRotationPoint(24.8F, -33F, -0.5F);
		baseModel[94].rotateAngleX = -0.80285146F;
		baseModel[94].rotateAngleZ = -0.10471976F;

		baseModel[95].addBox(0F, 0F, 0F, 1, 1, 8, 0F); // Box 119
		baseModel[95].setRotationPoint(25.2F, -33.2F, -0.5F);

		baseModel[96].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 120
		baseModel[96].setRotationPoint(25.5F, -33F, 8F);
		baseModel[96].rotateAngleX = -0.89011792F;
		baseModel[96].rotateAngleZ = 0.54105207F;

		baseModel[97].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 121
		baseModel[97].setRotationPoint(26.8F, -31.2F, 9F);
		baseModel[97].rotateAngleZ = 0.10471976F;

		baseModel[98].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 122
		baseModel[98].setRotationPoint(27.2F, -29F, 9F);
		baseModel[98].rotateAngleX = -0.59341195F;
		baseModel[98].rotateAngleZ = 0.10471976F;

		baseModel[99].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 123
		baseModel[99].setRotationPoint(27.8F, -27F, 8F);
		baseModel[99].rotateAngleX = -1.01229097F;
		baseModel[99].rotateAngleZ = 0.10471976F;

		baseModel[100].addBox(0F, 0F, 0F, 1, 1, 5, 0F); // Box 124
		baseModel[100].setRotationPoint(28.8F, -25.4F, 1F);

		baseModel[101].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 125
		baseModel[101].setRotationPoint(29.2F, -26F, -1F);
		baseModel[101].rotateAngleX = 1.01229097F;
		baseModel[101].rotateAngleZ = -0.10471976F;

		baseModel[102].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 126
		baseModel[102].setRotationPoint(29.8F, -28F, -2.5F);
		baseModel[102].rotateAngleX = 0.59341195F;
		baseModel[102].rotateAngleZ = -0.10471976F;

		baseModel[103].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 127
		baseModel[103].setRotationPoint(30.3F, -31F, -2.5F);
		baseModel[103].rotateAngleZ = -0.10471976F;

		baseModel[104].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 128
		baseModel[104].setRotationPoint(30.8F, -33F, -0.5F);
		baseModel[104].rotateAngleX = -0.80285146F;
		baseModel[104].rotateAngleZ = -0.10471976F;

		baseModel[105].addBox(0F, 0F, 0F, 1, 1, 8, 0F); // Box 129
		baseModel[105].setRotationPoint(31.2F, -33.2F, -0.5F);

		baseModel[106].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 130
		baseModel[106].setRotationPoint(30.5F, -33F, 8F);
		baseModel[106].rotateAngleX = -0.89011792F;
		baseModel[106].rotateAngleZ = 0.54105207F;

		baseModel[107].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 131
		baseModel[107].setRotationPoint(31.8F, -31.2F, 9F);
		baseModel[107].rotateAngleZ = 0.10471976F;

		baseModel[108].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 132
		baseModel[108].setRotationPoint(32.2F, -29F, 9F);
		baseModel[108].rotateAngleX = -0.59341195F;
		baseModel[108].rotateAngleZ = 0.10471976F;

		baseModel[109].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 133
		baseModel[109].setRotationPoint(32.8F, -27F, 8F);
		baseModel[109].rotateAngleX = -1.01229097F;
		baseModel[109].rotateAngleZ = 0.10471976F;

		baseModel[110].addBox(0F, 0F, 0F, 1, 1, 5, 0F); // Box 134
		baseModel[110].setRotationPoint(33.8F, -25.4F, 1F);

		baseModel[111].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 135
		baseModel[111].setRotationPoint(34.2F, -26F, -1F);
		baseModel[111].rotateAngleX = 1.01229097F;
		baseModel[111].rotateAngleZ = -0.10471976F;

		baseModel[112].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 136
		baseModel[112].setRotationPoint(34.8F, -28F, -2.5F);
		baseModel[112].rotateAngleX = 0.59341195F;
		baseModel[112].rotateAngleZ = -0.10471976F;

		baseModel[113].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 137
		baseModel[113].setRotationPoint(35.3F, -31F, -2.5F);
		baseModel[113].rotateAngleZ = -0.10471976F;

		baseModel[114].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 138
		baseModel[114].setRotationPoint(35.8F, -33F, -0.5F);
		baseModel[114].rotateAngleX = -0.80285146F;
		baseModel[114].rotateAngleZ = -0.10471976F;

		baseModel[115].addBox(0F, 0F, 0F, 1, 1, 8, 0F); // Box 139
		baseModel[115].setRotationPoint(36.2F, -33.2F, -0.5F);

		baseModel[116].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 140
		baseModel[116].setRotationPoint(35.5F, -33F, 8F);
		baseModel[116].rotateAngleX = -0.89011792F;
		baseModel[116].rotateAngleZ = 0.54105207F;

		baseModel[117].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 141
		baseModel[117].setRotationPoint(36.8F, -31.2F, 9F);
		baseModel[117].rotateAngleZ = 0.10471976F;

		baseModel[118].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 142
		baseModel[118].setRotationPoint(37.2F, -29F, 9F);
		baseModel[118].rotateAngleX = -0.59341195F;
		baseModel[118].rotateAngleZ = 0.10471976F;

		baseModel[119].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 143
		baseModel[119].setRotationPoint(37.8F, -27F, 8F);
		baseModel[119].rotateAngleX = -1.01229097F;
		baseModel[119].rotateAngleZ = 0.10471976F;

		baseModel[120].addBox(0F, 0F, 0F, 1, 1, 5, 0F); // Box 144
		baseModel[120].setRotationPoint(38.8F, -25.4F, 1F);

		baseModel[121].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 145
		baseModel[121].setRotationPoint(39.2F, -26F, -1F);
		baseModel[121].rotateAngleX = 1.01229097F;
		baseModel[121].rotateAngleZ = -0.10471976F;

		baseModel[122].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 146
		baseModel[122].setRotationPoint(39.8F, -28F, -2.5F);
		baseModel[122].rotateAngleX = 0.59341195F;
		baseModel[122].rotateAngleZ = -0.10471976F;

		baseModel[123].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 147
		baseModel[123].setRotationPoint(40.3F, -31F, -2.5F);
		baseModel[123].rotateAngleZ = -0.10471976F;

		baseModel[124].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 148
		baseModel[124].setRotationPoint(40.8F, -33F, -0.5F);
		baseModel[124].rotateAngleX = -0.80285146F;
		baseModel[124].rotateAngleZ = -0.10471976F;

		baseModel[125].addBox(0F, 0F, 0F, 1, 1, 8, 0F); // Box 149
		baseModel[125].setRotationPoint(41.2F, -33.2F, -0.5F);

		baseModel[126].addBox(0F, 0F, 0F, 1, 1, 3, 0F); // Box 150
		baseModel[126].setRotationPoint(41F, -33F, 8F);
		baseModel[126].rotateAngleX = -0.89011792F;
		baseModel[126].rotateAngleZ = 0.29670597F;

		baseModel[127].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 151
		baseModel[127].setRotationPoint(41.8F, -31.2F, 9F);
		baseModel[127].rotateAngleZ = 0.10471976F;

		baseModel[128].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 152
		baseModel[128].setRotationPoint(42.2F, -29F, 9F);
		baseModel[128].rotateAngleX = -0.59341195F;
		baseModel[128].rotateAngleZ = 0.10471976F;

		baseModel[129].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 153
		baseModel[129].setRotationPoint(42.8F, -27F, 8F);
		baseModel[129].rotateAngleX = -1.01229097F;
		baseModel[129].rotateAngleZ = 0.10471976F;

		baseModel[130].addBox(0F, 0F, 0F, 1, 1, 5, 0F); // Box 154
		baseModel[130].setRotationPoint(43.8F, -25.4F, 1F);

		baseModel[131].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 155
		baseModel[131].setRotationPoint(44.2F, -26F, -1F);
		baseModel[131].rotateAngleX = 1.01229097F;
		baseModel[131].rotateAngleZ = -0.10471976F;

		baseModel[132].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 156
		baseModel[132].setRotationPoint(44.8F, -28F, -2.5F);
		baseModel[132].rotateAngleX = 0.59341195F;
		baseModel[132].rotateAngleZ = -0.10471976F;

		baseModel[133].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 157
		baseModel[133].setRotationPoint(45.3F, -31F, -2.5F);
		baseModel[133].rotateAngleZ = -0.10471976F;

		baseModel[134].addBox(0F, 0F, 0F, 1, 3, 1, 0F); // Box 158
		baseModel[134].setRotationPoint(45.8F, -33F, -0.5F);
		baseModel[134].rotateAngleX = -0.80285146F;
		baseModel[134].rotateAngleZ = -0.10471976F;

		baseModel[135].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 160
		baseModel[135].setRotationPoint(12F, -28F, 44F);

		baseModel[136].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 166
		baseModel[136].setRotationPoint(28F, -28F, 44F);

		baseModel[137].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 167
		baseModel[137].setRotationPoint(36F, -28F, 45F);
		baseModel[137].rotateAngleY = -0.08726646F;

		baseModel[138].addBox(0F, 0F, 0F, 8, 4, 4, 0F); // Box 168
		baseModel[138].setRotationPoint(4F, -28F, 43F);
		baseModel[138].rotateAngleY = 0.12217305F;

		baseModel[139].addBox(0F, 0F, 0F, 4, 4, 16, 0F); // Box 169
		baseModel[139].setRotationPoint(0F, -28F, 27F);

		baseModel[140].addBox(0F, 0F, 0F, 4, 4, 8, 0F); // Box 170
		baseModel[140].setRotationPoint(0F, -28F, 12F);
		baseModel[140].rotateAngleY = 0.06981317F;

		baseModel[141].addBox(0F, 0F, 0F, 5, 4, 4, 0F); // Box 171
		baseModel[141].setRotationPoint(6F, -28F, 0F);

		baseModel[142].addBox(0F, 0F, 0F, 5, 4, 4, 0F); // Box 172
		baseModel[142].setRotationPoint(18F, -28F, 0F);
		baseModel[142].rotateAngleY = -0.08726646F;

		baseModel[143].addBox(0F, 0F, 0F, 5, 4, 4, 0F); // Box 173
		baseModel[143].setRotationPoint(30F, -28F, 0F);
		baseModel[143].rotateAngleY = 0.05235988F;

		baseModel[144].addBox(0F, 0F, 0F, 4, 4, 8, 0F); // Box 174
		baseModel[144].setRotationPoint(44F, -28F, 6F);
		baseModel[144].rotateAngleY = -0.13962634F;

		baseModel[145].addBox(0F, 0F, 0F, 4, 4, 16, 0F); // Box 175
		baseModel[145].setRotationPoint(44F, -28F, 14F);

		baseModel[146].addBox(0F, 0F, 0F, 4, 4, 8, 0F); // Box 176
		baseModel[146].setRotationPoint(45F, -28F, 36F);
		baseModel[146].rotateAngleY = 0.31415927F;

		baseModel[147].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 178
		baseModel[147].setRotationPoint(48F, -16F, 10F);
		baseModel[147].rotateAngleY = 0.03490659F;

		baseModel[148].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 179
		baseModel[148].setRotationPoint(48F, -20F, 36F);

		baseModel[149].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 180
		baseModel[149].setRotationPoint(48F, -12F, 30F);

		baseModel[150].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 181
		baseModel[150].setRotationPoint(48F, -20F, 0F);

		baseModel[151].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 184
		baseModel[151].setRotationPoint(0F, -12F, 8F);
		baseModel[151].rotateAngleY = 1.57079633F;

		baseModel[152].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 185
		baseModel[152].setRotationPoint(-1F, -24F, 32F);

		baseModel[153].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 186
		baseModel[153].setRotationPoint(-1F, -12F, 32F);

		baseModel[154].addBox(0F, 0F, 0F, 1, 4, 8, 0F); // Box 187
		baseModel[154].setRotationPoint(-1F, -16F, 40F);

		baseModel[155].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 188
		baseModel[155].setRotationPoint(8F, -20F, 48F);

		baseModel[156].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 189
		baseModel[156].setRotationPoint(24F, -23.9F, 48F);

		baseModel[157].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 190
		baseModel[157].setRotationPoint(17F, -16F, 48F);

		baseModel[158].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 191
		baseModel[158].setRotationPoint(33F, -12F, 48F);

		baseModel[159].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 192
		baseModel[159].setRotationPoint(40F, -20F, 48F);

		baseModel[160].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 193
		baseModel[160].setRotationPoint(4F, -12F, 48F);

		baseModel[161].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 194
		baseModel[161].setRotationPoint(4F, -12F, -1F);

		baseModel[162].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 195
		baseModel[162].setRotationPoint(21F, -12F, -1F);

		baseModel[163].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 196
		baseModel[163].setRotationPoint(9F, -16F, -1F);

		baseModel[164].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 197
		baseModel[164].setRotationPoint(20F, -20F, -1F);

		baseModel[165].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 198
		baseModel[165].setRotationPoint(36F, -16F, -1F);

		baseModel[166].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 199
		baseModel[166].setRotationPoint(4F, -24F, -1F);

		baseModel[167].addBox(0F, 0F, 0F, 8, 4, 1, 0F); // Box 200
		baseModel[167].setRotationPoint(40F, -24F, -1F);

		baseModel[168].addBox(0F, 0F, 0F, 1, 3, 3, 0F); // Radio23
		baseModel[168].setRotationPoint(13F, -24.5F, 16.5F);

		baseModel[169].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		baseModel[169].setRotationPoint(14F, -24F, 17F);

		baseModel[170].addBox(0F, 0F, 0F, 2, 7, 2, 0F); // Radio23
		baseModel[170].setRotationPoint(14F, -22F, 17F);

		baseModel[171].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // Radio23
		baseModel[171].setRotationPoint(14F, -15F, 17F);

		baseModel[172].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Radio23
		baseModel[172].setRotationPoint(16F, -15F, 17F);

		baseModel[173].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		baseModel[173].setRotationPoint(17F, -15F, 17F);

		baseModel[174].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F); // Radio23
		baseModel[174].setRotationPoint(17F, -10F, 17F);

		baseModel[175].addBox(0F, 0F, 0F, 2, 3, 2, 0F); // Radio23
		baseModel[175].setRotationPoint(17F, -13F, 17F);

		baseModel[176].addBox(0F, 0F, 0F, 8, 2, 2, 0F); // Radio23
		baseModel[176].setRotationPoint(19F, -10F, 17F);

		baseModel[177].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F); // Radio23
		baseModel[177].setRotationPoint(27F, -10F, 17F);

		baseModel[178].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		baseModel[178].setRotationPoint(27F, -10F, 13F);

		baseModel[179].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // Radio23
		baseModel[179].setRotationPoint(27F, -10F, 15F);

		baseModel[180].addBox(0F, 0F, 0F, 2, 6, 2, 0F); // Radio23
		baseModel[180].setRotationPoint(27F, -16F, 13F);

		baseModel[181].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		baseModel[181].setRotationPoint(27F, -18F, 13F);

		baseModel[182].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		baseModel[182].setRotationPoint(27F, -18F, 9F);

		baseModel[183].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F); // Radio23
		baseModel[183].setRotationPoint(29F, -18F, 9F);

		baseModel[184].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		baseModel[184].setRotationPoint(29F, -24F, 9F);

		baseModel[185].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		baseModel[185].setRotationPoint(37F, -24F, 9F);

		baseModel[186].addBox(0F, 0F, 0F, 2, 2, 2, 0F); // Radio23
		baseModel[186].setRotationPoint(27F, -18F, 11F);

		baseModel[187].addBox(0F, 0F, 0F, 2, 4, 2, 0F); // Radio23
		baseModel[187].setRotationPoint(29F, -22F, 9F);

		baseModel[188].addBox(0F, 0F, 0F, 6, 2, 2, 0F); // Radio23
		baseModel[188].setRotationPoint(31F, -24F, 9F);

		baseModel[189].addBox(0F, 0F, 0F, 2, 2, 6, 0F); // Radio23
		baseModel[189].setRotationPoint(37F, -24F, 11F);

		baseModel[190].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 0F, 1F, 0F); // Radio23
		baseModel[190].setRotationPoint(37F, -24F, 17F);

		baseModel[191].addBox(0F, 0F, 0F, 2, 2, 6, 0F); // Radio23
		baseModel[191].setRotationPoint(37F, -23F, 19F);

		baseModel[192].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F); // Radio23
		baseModel[192].setRotationPoint(37F, -23F, 25F);

		baseModel[193].addShapeBox(0F, 0F, 0F, 2, 2, 2, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -0.5F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Radio23
		baseModel[193].setRotationPoint(40F, -23F, 25F);

		baseModel[194].addBox(0F, 0F, 0F, 2, 2, 5, 0F); // Radio23
		baseModel[194].setRotationPoint(40F, -23F, 27F);

		baseModel[195].addBox(0F, 0F, 0F, 1, 2, 2, 0F); // Radio23
		baseModel[195].setRotationPoint(39F, -23F, 25F);

		baseModel[196].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 184
		baseModel[196].setRotationPoint(0F, -20F, 8F);
		baseModel[196].rotateAngleY = 1.57079633F;

		baseModel[197].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 184
		baseModel[197].setRotationPoint(0F, -16F, 16F);
		baseModel[197].rotateAngleY = 1.57079633F;

		baseModel[198].addBox(0F, 0F, 0F, 16, 4, 1, 0F); // Box 184
		baseModel[198].setRotationPoint(49F, -20F, 14F);
		baseModel[198].rotateAngleY = 1.57079633F;

		baseModel[199].addBox(-0.5F, -1F, 0F, 1, 2, 2, 0F); // Jerrycans12
		baseModel[199].setRotationPoint(21F, -21F, 41.2F);
		baseModel[199].rotateAngleY = 1.25663706F;
		baseModel[199].rotateAngleZ = 0.62831853F;

		baseModel[200].addBox(-0.5F, -1F, 0F, 1, 2, 2, 0F); // Jerrycans12
		baseModel[200].setRotationPoint(24F, -21F, 43F);
		baseModel[200].rotateAngleY = -3.14159265F;
		baseModel[200].rotateAngleZ = 0.62831853F;

		baseModel[201].addBox(-0.5F, -1F, 0F, 1, 2, 2, 0F); // Jerrycans12
		baseModel[201].setRotationPoint(24F, -21F, 38.5F);
		baseModel[201].rotateAngleY = -3.14159265F;
		baseModel[201].rotateAngleZ = 0.62831853F;

		baseModel[202].addBox(0F, 0F, 0F, 0, 2, 8, 0F); // Box 149
		baseModel[202].setRotationPoint(41.7F, -33.7F, -0.5F);

		baseModel[203].addBox(0F, 0F, 0F, 0, 2, 8, 0F); // Box 149
		baseModel[203].setRotationPoint(36.7F, -33.7F, -0.5F);

		baseModel[204].addBox(0F, 0F, 0F, 0, 2, 8, 0F); // Box 149
		baseModel[204].setRotationPoint(31.7F, -33.7F, -0.5F);

		baseModel[205].addBox(0F, 0F, 0F, 0, 2, 8, 0F); // Box 149
		baseModel[205].setRotationPoint(25.7F, -33.7F, -0.5F);

		baseModel[206].addBox(0F, 0F, 0F, 0, 2, 8, 0F); // Box 149
		baseModel[206].setRotationPoint(19.7F, -33.7F, -0.5F);

		baseModel[207].addBox(0F, 0F, 0F, 0, 2, 8, 0F); // Box 149
		baseModel[207].setRotationPoint(13.7F, -33.7F, -0.5F);

		baseModel[208].addBox(0F, 0F, 0F, 0, 2, 8, 0F); // Box 149
		baseModel[208].setRotationPoint(7.7F, -33.7F, -0.5F);

		baseModel[209].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 88
		baseModel[209].setRotationPoint(45.8F, -33F, -0.5F);
		baseModel[209].rotateAngleX = -0.80285146F;
		baseModel[209].rotateAngleZ = -0.10471976F;

		baseModel[210].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 87
		baseModel[210].setRotationPoint(45.3F, -31F, -2.5F);
		baseModel[210].rotateAngleZ = -0.10471976F;

		baseModel[211].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 86
		baseModel[211].setRotationPoint(44.8F, -28F, -2.5F);
		baseModel[211].rotateAngleX = 0.59341195F;
		baseModel[211].rotateAngleZ = -0.10471976F;

		baseModel[212].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 85
		baseModel[212].setRotationPoint(44.2F, -26F, -1F);
		baseModel[212].rotateAngleX = 1.01229097F;
		baseModel[212].rotateAngleZ = -0.10471976F;

		baseModel[213].addBox(0.5F, -0.5F, 0F, 0, 2, 5, 0F); // Box 84
		baseModel[213].setRotationPoint(43.8F, -25.4F, 1F);

		baseModel[214].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 83
		baseModel[214].setRotationPoint(42.8F, -27F, 8F);
		baseModel[214].rotateAngleX = -1.01229097F;
		baseModel[214].rotateAngleZ = 0.10471976F;

		baseModel[215].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 82
		baseModel[215].setRotationPoint(42.2F, -29F, 9F);
		baseModel[215].rotateAngleX = -0.59341195F;
		baseModel[215].rotateAngleZ = 0.10471976F;

		baseModel[216].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 81
		baseModel[216].setRotationPoint(41.8F, -31.2F, 9F);
		baseModel[216].rotateAngleZ = 0.10471976F;

		baseModel[217].addBox(0.5F, -0.5F, 0F, 0, 2, 3, 0F); // Box 80
		baseModel[217].setRotationPoint(40.5F, -33F, 8F);
		baseModel[217].rotateAngleX = -0.89011792F;
		baseModel[217].rotateAngleZ = 0.54105207F;

		baseModel[218].addBox(0F, 0F, 0F, 0, 2, 8, 0F); // Box 149
		baseModel[218].setRotationPoint(31.7F, -33.7F, -0.5F);

		baseModel[219].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 88
		baseModel[219].setRotationPoint(30.8F, -33F, -0.5F);
		baseModel[219].rotateAngleX = -0.80285146F;
		baseModel[219].rotateAngleZ = -0.10471976F;

		baseModel[220].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 87
		baseModel[220].setRotationPoint(30.3F, -31F, -2.5F);
		baseModel[220].rotateAngleZ = -0.10471976F;

		baseModel[221].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 86
		baseModel[221].setRotationPoint(29.8F, -28F, -2.5F);
		baseModel[221].rotateAngleX = 0.59341195F;
		baseModel[221].rotateAngleZ = -0.10471976F;

		baseModel[222].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 85
		baseModel[222].setRotationPoint(29.2F, -26F, -1F);
		baseModel[222].rotateAngleX = 1.01229097F;
		baseModel[222].rotateAngleZ = -0.10471976F;

		baseModel[223].addBox(0.5F, -0.5F, 0F, 0, 2, 5, 0F); // Box 84
		baseModel[223].setRotationPoint(28.8F, -25.4F, 1F);

		baseModel[224].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 83
		baseModel[224].setRotationPoint(27.8F, -27F, 8F);
		baseModel[224].rotateAngleX = -1.01229097F;
		baseModel[224].rotateAngleZ = 0.10471976F;

		baseModel[225].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 82
		baseModel[225].setRotationPoint(27.2F, -29F, 9F);
		baseModel[225].rotateAngleX = -0.59341195F;
		baseModel[225].rotateAngleZ = 0.10471976F;

		baseModel[226].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 81
		baseModel[226].setRotationPoint(26.8F, -31.2F, 9F);
		baseModel[226].rotateAngleZ = 0.10471976F;

		baseModel[227].addBox(0.5F, -0.5F, 0F, 0, 2, 3, 0F); // Box 80
		baseModel[227].setRotationPoint(25.5F, -33F, 8F);
		baseModel[227].rotateAngleX = -0.89011792F;
		baseModel[227].rotateAngleZ = 0.54105207F;

		baseModel[228].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 88
		baseModel[228].setRotationPoint(12.8F, -33F, -0.5F);
		baseModel[228].rotateAngleX = -0.80285146F;
		baseModel[228].rotateAngleZ = -0.10471976F;

		baseModel[229].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 87
		baseModel[229].setRotationPoint(12.3F, -31F, -2.5F);
		baseModel[229].rotateAngleZ = -0.10471976F;

		baseModel[230].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 86
		baseModel[230].setRotationPoint(11.8F, -28F, -2.5F);
		baseModel[230].rotateAngleX = 0.59341195F;
		baseModel[230].rotateAngleZ = -0.10471976F;

		baseModel[231].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 85
		baseModel[231].setRotationPoint(11.2F, -26F, -1F);
		baseModel[231].rotateAngleX = 1.01229097F;
		baseModel[231].rotateAngleZ = -0.10471976F;

		baseModel[232].addBox(0.5F, -0.5F, 0F, 0, 2, 5, 0F); // Box 84
		baseModel[232].setRotationPoint(10.8F, -25.4F, 1F);

		baseModel[233].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 83
		baseModel[233].setRotationPoint(9.8F, -27F, 8F);
		baseModel[233].rotateAngleX = -1.01229097F;
		baseModel[233].rotateAngleZ = 0.10471976F;

		baseModel[234].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 82
		baseModel[234].setRotationPoint(9.2F, -29F, 9F);
		baseModel[234].rotateAngleX = -0.59341195F;
		baseModel[234].rotateAngleZ = 0.10471976F;

		baseModel[235].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 81
		baseModel[235].setRotationPoint(8.8F, -31.2F, 9F);
		baseModel[235].rotateAngleZ = 0.10471976F;

		baseModel[236].addBox(0.5F, -0.5F, 0F, 0, 2, 3, 0F); // Box 80
		baseModel[236].setRotationPoint(7.5F, -33F, 8F);
		baseModel[236].rotateAngleX = -0.89011792F;
		baseModel[236].rotateAngleZ = 0.54105207F;

		baseModel[237].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 88
		baseModel[237].setRotationPoint(18.8F, -33F, -0.5F);
		baseModel[237].rotateAngleX = -0.80285146F;
		baseModel[237].rotateAngleZ = -0.10471976F;

		baseModel[238].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 87
		baseModel[238].setRotationPoint(18.3F, -31F, -2.5F);
		baseModel[238].rotateAngleZ = -0.10471976F;

		baseModel[239].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 86
		baseModel[239].setRotationPoint(17.8F, -28F, -2.5F);
		baseModel[239].rotateAngleX = 0.59341195F;
		baseModel[239].rotateAngleZ = -0.10471976F;

		baseModel[240].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 85
		baseModel[240].setRotationPoint(17.2F, -26F, -1F);
		baseModel[240].rotateAngleX = 1.01229097F;
		baseModel[240].rotateAngleZ = -0.10471976F;

		baseModel[241].addBox(0.5F, -0.5F, 0F, 0, 2, 5, 0F); // Box 84
		baseModel[241].setRotationPoint(16.8F, -25.4F, 1F);

		baseModel[242].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 83
		baseModel[242].setRotationPoint(15.8F, -27F, 8F);
		baseModel[242].rotateAngleX = -1.01229097F;
		baseModel[242].rotateAngleZ = 0.10471976F;

		baseModel[243].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 82
		baseModel[243].setRotationPoint(15.2F, -29F, 9F);
		baseModel[243].rotateAngleX = -0.59341195F;
		baseModel[243].rotateAngleZ = 0.10471976F;

		baseModel[244].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 81
		baseModel[244].setRotationPoint(14.8F, -31.2F, 9F);
		baseModel[244].rotateAngleZ = 0.10471976F;

		baseModel[245].addBox(0.5F, -0.5F, 0F, 0, 2, 3, 0F); // Box 80
		baseModel[245].setRotationPoint(13.5F, -33F, 8F);
		baseModel[245].rotateAngleX = -0.89011792F;
		baseModel[245].rotateAngleZ = 0.54105207F;

		baseModel[246].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 88
		baseModel[246].setRotationPoint(24.8F, -33F, -0.5F);
		baseModel[246].rotateAngleX = -0.80285146F;
		baseModel[246].rotateAngleZ = -0.10471976F;

		baseModel[247].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 87
		baseModel[247].setRotationPoint(24.3F, -31F, -2.5F);
		baseModel[247].rotateAngleZ = -0.10471976F;

		baseModel[248].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 86
		baseModel[248].setRotationPoint(23.8F, -28F, -2.5F);
		baseModel[248].rotateAngleX = 0.59341195F;
		baseModel[248].rotateAngleZ = -0.10471976F;

		baseModel[249].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 85
		baseModel[249].setRotationPoint(23.2F, -26F, -1F);
		baseModel[249].rotateAngleX = 1.01229097F;
		baseModel[249].rotateAngleZ = -0.10471976F;

		baseModel[250].addBox(0.5F, -0.5F, 0F, 0, 2, 5, 0F); // Box 84
		baseModel[250].setRotationPoint(22.8F, -25.4F, 1F);

		baseModel[251].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 83
		baseModel[251].setRotationPoint(21.8F, -27F, 8F);
		baseModel[251].rotateAngleX = -1.01229097F;
		baseModel[251].rotateAngleZ = 0.10471976F;

		baseModel[252].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 82
		baseModel[252].setRotationPoint(21.2F, -29F, 9F);
		baseModel[252].rotateAngleX = -0.59341195F;
		baseModel[252].rotateAngleZ = 0.10471976F;

		baseModel[253].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 81
		baseModel[253].setRotationPoint(20.8F, -31.2F, 9F);
		baseModel[253].rotateAngleZ = 0.10471976F;

		baseModel[254].addBox(0.5F, -0.5F, 0F, 0, 2, 3, 0F); // Box 80
		baseModel[254].setRotationPoint(19.5F, -33F, 8F);
		baseModel[254].rotateAngleX = -0.89011792F;
		baseModel[254].rotateAngleZ = 0.54105207F;

		baseModel[255].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 88
		baseModel[255].setRotationPoint(35.8F, -33F, -0.5F);
		baseModel[255].rotateAngleX = -0.80285146F;
		baseModel[255].rotateAngleZ = -0.10471976F;

		baseModel[256].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 87
		baseModel[256].setRotationPoint(35.3F, -31F, -2.5F);
		baseModel[256].rotateAngleZ = -0.10471976F;

		baseModel[257].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 86
		baseModel[257].setRotationPoint(34.8F, -28F, -2.5F);
		baseModel[257].rotateAngleX = 0.59341195F;
		baseModel[257].rotateAngleZ = -0.10471976F;

		baseModel[258].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 85
		baseModel[258].setRotationPoint(34.2F, -26F, -1F);
		baseModel[258].rotateAngleX = 1.01229097F;
		baseModel[258].rotateAngleZ = -0.10471976F;

		baseModel[259].addBox(0.5F, -0.5F, 0F, 0, 2, 5, 0F); // Box 84
		baseModel[259].setRotationPoint(33.8F, -25.4F, 1F);

		baseModel[260].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 83
		baseModel[260].setRotationPoint(32.8F, -27F, 8F);
		baseModel[260].rotateAngleX = -1.01229097F;
		baseModel[260].rotateAngleZ = 0.10471976F;

		baseModel[261].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 82
		baseModel[261].setRotationPoint(32.2F, -29F, 9F);
		baseModel[261].rotateAngleX = -0.59341195F;
		baseModel[261].rotateAngleZ = 0.10471976F;

		baseModel[262].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 81
		baseModel[262].setRotationPoint(31.8F, -31.2F, 9F);
		baseModel[262].rotateAngleZ = 0.10471976F;

		baseModel[263].addBox(0.5F, -0.5F, 0F, 0, 2, 3, 0F); // Box 80
		baseModel[263].setRotationPoint(30.5F, -33F, 8F);
		baseModel[263].rotateAngleX = -0.89011792F;
		baseModel[263].rotateAngleZ = 0.54105207F;

		baseModel[264].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 88
		baseModel[264].setRotationPoint(40.8F, -33F, -0.5F);
		baseModel[264].rotateAngleX = -0.80285146F;
		baseModel[264].rotateAngleZ = -0.10471976F;

		baseModel[265].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 87
		baseModel[265].setRotationPoint(40.3F, -31F, -2.5F);
		baseModel[265].rotateAngleZ = -0.10471976F;

		baseModel[266].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 86
		baseModel[266].setRotationPoint(39.8F, -28F, -2.5F);
		baseModel[266].rotateAngleX = 0.59341195F;
		baseModel[266].rotateAngleZ = -0.10471976F;

		baseModel[267].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 85
		baseModel[267].setRotationPoint(39.2F, -26F, -1F);
		baseModel[267].rotateAngleX = 1.01229097F;
		baseModel[267].rotateAngleZ = -0.10471976F;

		baseModel[268].addBox(0.5F, -0.5F, 0F, 0, 2, 5, 0F); // Box 84
		baseModel[268].setRotationPoint(38.8F, -25.4F, 1F);

		baseModel[269].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 83
		baseModel[269].setRotationPoint(37.8F, -27F, 8F);
		baseModel[269].rotateAngleX = -1.01229097F;
		baseModel[269].rotateAngleZ = 0.10471976F;

		baseModel[270].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 82
		baseModel[270].setRotationPoint(37.2F, -29F, 9F);
		baseModel[270].rotateAngleX = -0.59341195F;
		baseModel[270].rotateAngleZ = 0.10471976F;

		baseModel[271].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 81
		baseModel[271].setRotationPoint(36.8F, -31.2F, 9F);
		baseModel[271].rotateAngleZ = 0.10471976F;

		baseModel[272].addBox(0.5F, -0.5F, 0F, 0, 2, 3, 0F); // Box 80
		baseModel[272].setRotationPoint(35.5F, -33F, 8F);
		baseModel[272].rotateAngleX = -0.89011792F;
		baseModel[272].rotateAngleZ = 0.54105207F;

		baseModel[273].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 88
		baseModel[273].setRotationPoint(6.8F, -33F, -0.5F);
		baseModel[273].rotateAngleX = -0.80285146F;
		baseModel[273].rotateAngleZ = -0.10471976F;

		baseModel[274].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 87
		baseModel[274].setRotationPoint(6.3F, -31F, -2.5F);
		baseModel[274].rotateAngleZ = -0.10471976F;

		baseModel[275].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 86
		baseModel[275].setRotationPoint(5.8F, -28F, -2.5F);
		baseModel[275].rotateAngleX = 0.59341195F;
		baseModel[275].rotateAngleZ = -0.10471976F;

		baseModel[276].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 85
		baseModel[276].setRotationPoint(5.2F, -26F, -1F);
		baseModel[276].rotateAngleX = 1.01229097F;
		baseModel[276].rotateAngleZ = -0.10471976F;

		baseModel[277].addBox(0.5F, -0.5F, 0F, 0, 2, 5, 0F); // Box 84
		baseModel[277].setRotationPoint(4.8F, -25.4F, 1F);

		baseModel[278].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 83
		baseModel[278].setRotationPoint(3.8F, -27F, 8F);
		baseModel[278].rotateAngleX = -1.01229097F;
		baseModel[278].rotateAngleZ = 0.10471976F;

		baseModel[279].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 82
		baseModel[279].setRotationPoint(3.2F, -29F, 9F);
		baseModel[279].rotateAngleX = -0.59341195F;
		baseModel[279].rotateAngleZ = 0.10471976F;

		baseModel[280].addBox(0.5F, 0F, -0.5F, 0, 3, 2, 0F); // Box 81
		baseModel[280].setRotationPoint(2.8F, -31.2F, 9F);
		baseModel[280].rotateAngleZ = 0.10471976F;

		baseModel[281].addBox(0.5F, -0.5F, 0F, 0, 2, 3, 0F); // Box 80
		baseModel[281].setRotationPoint(1.5F, -33F, 8F);
		baseModel[281].rotateAngleX = -0.89011792F;
		baseModel[281].rotateAngleZ = 0.54105207F;

		baseModel[282].addBox(0.5F, -0.5F, 0F, 0, 2, 3, 0F); // Box 79
		baseModel[282].setRotationPoint(1.5F, -33F, 5F);
		baseModel[282].rotateAngleY = -0.08726646F;

		flipAll();
		translateAll(-24,-8f,24);
	}
}
