/*************************************************************************
 *                                                                       *
 * Open Dynamics Engine, Copyright (C) 2001,2002 Russell L. Smith.       *
 * All rights reserved.  Email: russ@q12.org   Web: www.q12.org          *
 * Open Dynamics Engine 4J, Copyright (C) 2009-2014 Tilmann Zaeschke     *
 * All rights reserved.  Email: ode4j@gmx.de   Web: www.ode4j.org        *
 *                                                                       *
 * This library is free software; you can redistribute it and/or         *
 * modify it under the terms of EITHER:                                  *
 *   (1) The GNU Lesser General Public License as published by the Free  *
 *       Software Foundation; either version 2.1 of the License, or (at  *
 *       your option) any later version. The text of the GNU Lesser      *
 *       General Public License is included with this library in the     *
 *       file LICENSE.TXT.                                               *
 *   (2) The BSD-style license that is included with this library in     *
 *       the file ODE-LICENSE-BSD.TXT and ODE4J-LICENSE-BSD.TXT.         *
 *                                                                       *
 * This library is distributed in the hope that it will be useful,       *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the files    *
 * LICENSE.TXT, ODE-LICENSE-BSD.TXT and ODE4J-LICENSE-BSD.TXT for more   *
 * details.                                                              *
 *                                                                       *
 *************************************************************************/
package org.ode4j.demo;

public class BasketGeom {

	static float world_normals[] = {
	0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,
	-0,0,1,0,0,1,0,0,1,0,0,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,-0,1,0,
	0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,1,0,-0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,-1,0,0,-1,
	0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,0,
	-0.948064f,0.318080f,0,-0.989482f,0.144655f,0,-0.983494f,0.180939f,
	0,-0.983494f,0.180939f,0,-0.908999f,0.416798f,0,-0.948064f,0.318080f,
	0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,
	0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,-1,
	0,-0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0
	,1,0,0,1,0,0,1,0,0,
	-0.132460f,0.991188f,0,0.264920f,0.964270f,0,0.132460f,0.991188f,0,
	0.132460f,0.991188f,0,-0.264920f,0.964270f,0,-0.132460f,0.991188f,0,-1,0,0,
	-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,0,1,0,0,1,0,0,1,0,
	0,1,0,0,1,0,0,1,0,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,-1,0,0,-1,0,0,
	-1,0,0,-1,0,0,-1,0,0,-1,0,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,1,0,0,1,0,
	0,1,0,0,1,0,0,1,0,0,1,0,0,-0.687592f,-0.726097f,-0,-0.881727f,-0.471761f,0,
	-0.687592f,-0.726097f,-0,-0.881727f,-0.471761f,0,-0.881727f,-0.471761f,0,
	-0.687592f,-0.726097f,-0,0.687592f,-0.726097f,0,0.928375f,-0.371644f,0,
	0.824321f,-0.566123f,0,0.687592f,-0.726097f,0,0.824321f,-0.566123f,0,
	0.687592f,-0.726097f,0,-0.881727f,-0.471761f,0,-0.985594f,-0.169128f,0,
	-0.985594f,-0.169128f,0,-0.985594f,-0.169128f,0,-0.881727f,-0.471761f,0,
	-0.881727f,-0.471761f,0,0.928375f,-0.371644f,0,0.985594f,-0.169128f,0,
	0.985594f,-0.169128f,0,0.928375f,-0.371644f,0,0.985594f,-0.169128f,0,
	0.824321f,-0.566123f,0,-0.870167f,0.492758f,0,-0.870167f,0.492758f,0,
	-0.870167f,0.492758f,0,-0.870167f,0.492758f,0,-0.870167f,0.492758f,0,
	-0.870167f,0.492758f,0,0.870167f,0.492758f,0,0.870167f,0.492758f,0,
	0.870167f,0.492758f,0,0.870167f,0.492758f,0,0.870167f,0.492758f,0,
	0.870167f,0.492758f,-0,-0.390313f,0.920682f,0,-0.132460f,0.991188f,0,
	-0.264920f,0.964270f,0,-0.264920f,0.964270f,0,-0.390313f,0.920682f,0,
	-0.390313f,0.920682f,0,0.390313f,0.920682f,0,0.132460f,0.991188f,0,
	0.264920f,0.964270f,0,0.390313f,0.920682f,0,0.264920f,0.964270f,0,
	0.390313f,0.920682f,-0,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,
	0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,
	0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,
	-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,
	0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,
	0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,
	1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,
	0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,
	0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,
	1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,
	0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,
	0.985594f,0.169128f,0,0.824321f,0.566123f,0,0.928375f,0.371644f,0,
	0.928375f,0.371644f,0,0.985594f,0.169128f,0,0.985594f,0.169128f,0,
	0.824321f,0.566123f,0,0.687592f,0.726097f,0,0.687592f,0.726097f,0,
	0.687592f,0.726097f,0,0.928375f,0.371644f,0,0.824321f,0.566123f,0,0,1,0,-0,
	1,0,0,1,0,0,1,0,0,1,0,0,1,0,-0.687592f,0.726097f,0,-0.687592f,0.726097f,0,
	-0.881727f,0.471761f,0,-0.881727f,0.471761f,0,-0.881727f,0.471761f,0,
	-0.687592f,0.726097f,0,-0.881727f,0.471761f,0,-0.985594f,0.169128f,0,
	-0.985594f,0.169128f,0,-0.985594f,0.169128f,0,-0.881727f,0.471761f,0,
	-0.881727f,0.471761f,0,-0.870166f,-0.492758f,0,-0.870166f,-0.492758f,0,
	-0.870166f,-0.492758f,0,-0.870166f,-0.492758f,0,-0.870166f,-0.492758f,0,
	-0.870166f,-0.492758f,0,-0.390314f,-0.920682f,0,-0.132460f,-0.991188f,0,
	-0.264921f,-0.964270f,0,-0.264921f,-0.964270f,0,-0.390314f,-0.920682f,0,
	-0.390314f,-0.920682f,0,-0.132460f,-0.991188f,0,0.264921f,-0.964270f,0,
	0.132460f,-0.991188f,0,0.132460f,-0.991188f,0,-0.264921f,-0.964270f,0,
	-0.132460f,-0.991188f,0,0.264921f,-0.964270f,0,0.390314f,-0.920682f,0,
	0.390314f,-0.920682f,0,0.390314f,-0.920682f,0,0.132460f,-0.991188f,0,
	0.264921f,-0.964270f,0,0.870166f,-0.492758f,0,0.870166f,-0.492758f,0,
	0.870166f,-0.492758f,0,0.870166f,-0.492758f,0,0.870166f,-0.492758f,0,
	0.870166f,-0.492758f,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,
	1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,
	0,0,1,0,0,1,0,0,1,0,-0.527606f,0.849489f,0,-0.793893f,0.608057f,0,
	-0.715135f,0.698986f,0,-0.715135f,0.698986f,0,-0.418249f,0.908332f,0,
	-0.527606f,0.849489f,0,-0.075284f,0.997162f,0,-0.253577f,0.967315f,0,
	-0.202069f,0.979371f,0,-0.202069f,0.979371f,0,-0.075284f,0.997162f,0,
	-0.075284f,0.997162f,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,
	0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,
	0.160137f,0.987095f,0,0.049305f,0.998784f,0,0.049305f,0.998784f,0,
	0.049305f,0.998784f,0,0.221401f,0.975183f,0,0.160137f,0.987095f,0,
	0.696124f,0.717921f,0,0.696124f,0.717921f,0,0.433340f,0.901230f,0,
	0.433340f,0.901230f,0,0.433340f,0.901230f,0,0.696124f,0.717921f,0,
	0.696124f,0.717921f,0,0.696124f,0.717921f,0,0.838308f,0.545197f,0,
	0.696124f,0.717921f,0,0.872167f,0.489208f,0,0.838308f,0.545197f,0,
	-0.994126f,0.108225f,0,-0.983494f,0.180939f,0,-0.989482f,0.144655f,0,
	-0.994126f,0.108225f,0,-0.989482f,0.144655f,0,-0.994126f,0.108225f,0,
	-0.948064f,0.318080f,0,-0.908999f,0.416798f,0,-0.793893f,0.608057f,0,
	-0.908999f,0.416798f,0,-0.715135f,0.698986f,0,-0.793893f,0.608057f,0,
	-0.527606f,0.849489f,0,-0.418249f,0.908332f,0,-0.253577f,0.967315f,0,
	-0.418249f,0.908332f,0,-0.202069f,0.979371f,0,-0.253577f,0.967315f,0,
	-0.075284f,0.997162f,0,-0.075284f,0.997162f,0,0,1,0,-0.075284f,0.997162f,0,
	0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,
	1,0,0,1,0,0,1,0,0,1,0,0.049305f,0.998784f,0,0,1,0,0.049305f,0.998784f,0,
	0.049305f,0.998784f,0,0.160137f,0.987095f,0,0.221401f,0.975183f,0,
	0.433340f,0.901230f,0,0.221401f,0.975183f,0,0.433340f,0.901230f,0,
	0.433340f,0.901230f,0,0.902172f,0.431376f,0,0.838308f,0.545197f,0,
	0.872167f,0.489208f,0,0.872167f,0.489208f,0,0.902172f,0.431376f,
	0,0.902172f,0.431376f
	};

	static float world_vertices[] = {
	-4,-4,-0.100000f,4,-4,-0.100000f,4,-4,0.100000f,-4,-4,-0.100000f,
	4,-4,0.100000f,-4,-4,0.100000f,4,0,0.100000f,4,-4,-0.100000f,
	4,4,-0.100000f,4,0,0.100000f,4,4,-0.100000f,4,4,0.100000f,
	4,0,0.100000f,4,-4,0.100000f,4,-4,-0.100000f,-4,-4,-0.100000f,
	-4,4,-0.100000f,4,4,-0.100000f,-4,-4,-0.100000f,4,4,-0.100000f,
	4,-4,-0.100000f,0.066000f,-2.060000f,2,0.066000f,-1.940000f,2,
	-0.066000f,-2.060000f,2,0.066000f,-1.940000f,2,
	-0.066000f,-1.940000f,2,-0.066000f,-2.060000f,2,
	-4,4,0.100000f,4,4,0.100000f,4,4,-0.100000f,4,4,
	-0.100000f,-4,4,-0.100000f,-4,4,0.100000f,-4,-4,
	0.100000f,-4,0,0.100000f,-4,-4,-0.100000f,-4,0,
	0.100000f,-4,4,0.100000f,-4,4,-0.100000f,-4,0,
	0.100000f,-4,4,-0.100000f,-4,-4,-0.100000f,
	0.360000f,3.244444f,1.466974f,0.360000f,3.422222f,2.266974f,
	-0.360000f,3.422222f,2.266974f,-0.360000f,3.422222f,2.266974f,
	-0.360000f,3.244444f,1.466974f,0.360000f,3.244444f,1.466974f,
	4,-4,0.100000f,0.066000f,-2.060000f,0.100000f,
	-0.066000f,-2.060000f,0.100000f,-0.066000f,-2.060000f,0.100000f,
	-4,-4,0.100000f,4,-4,0.100000f,4,0,0.100000f,
	0.066000f,-1.940000f,0.100000f,4,-4,0.100000f,
	0.066000f,-1.940000f,0.100000f,0.066000f,-2.060000f,0.100000f,
	4,-4,0.100000f,-0.066000f,-1.940000f,0.100000f,0.066000f,-1.940000f,
	
	0.100000f,4,0,0.100000f,4,0,0.100000f,-4,0,0.100000f,-0.066000f,-1.940000f,
	0.100000f,-0.066000f,-2.060000f,0.100000f,-0.066000f,-1.940000f,0.100000f,
	-4,0,0.100000f,-4,0,0.100000f,-4,-4,0.100000f,-0.066000f,-2.060000f,
	0.100000f,0.066000f,-2.060000f,2,-0.066000f,-2.060000f,2,-0.066000f,
	-2.060000f,0.100000f,-0.066000f,-2.060000f,0.100000f,0.066000f,-2.060000f,
	0.100000f,0.066000f,-2.060000f,2,0.066000f,-1.940000f,1.950000f,0.066000f,
	-1.940000f,2,0.066000f,-2.060000f,2,0.066000f,-2.060000f,2,0.066000f,
	-2.060000f,0.100000f,0.066000f,-1.940000f,1.950000f,0.066000f,-2.060000f,
	0.100000f,0.066000f,-1.940000f,0.100000f,0.066000f,-1.940000f,1.950000f,
	-0.052853f,-1.506390f,2,0.052853f,-1.506390f,2,0.052853f,-1.506390f,
	1.950000f,0.052853f,-1.506390f,1.950000f,-0.052853f,-1.506390f,1.950000f,
	-0.052853f,-1.506390f,2,-0.066000f,-2.060000f,0.100000f,-0.066000f,
	-2.060000f,2,-0.066000f,-1.940000f,1.950000f,-0.066000f,-2.060000f,
	0.100000f,-0.066000f,-1.940000f,1.950000f,-0.066000f,-1.940000f,0.100000f,
	-0.066000f,-2.060000f,2,-0.066000f,-1.940000f,2,-0.066000f,-1.940000f,
	1.950000f,-0.066000f,-1.940000f,0.100000f,-0.066000f,-1.940000f,1.950000f,
	0.066000f,-1.940000f,1.950000f,-0.066000f,-1.940000f,0.100000f,0.066000f,
	-1.940000f,1.950000f,0.066000f,-1.940000f,0.100000f,-0.066000f,-1.940000f,
	1.950000f,-0.066000f,-1.840000f,1.950000f,0.066000f,-1.940000f,1.950000f,
	-0.066000f,-1.840000f,1.950000f,0.066000f,-1.840000f,1.950000f,0.066000f,
	-1.940000f,1.950000f,-0.066000f,-1.940000f,2,-0.066000f,-1.840000f,2,
	-0.066000f,-1.840000f,1.950000f,-0.066000f,-1.840000f,1.950000f,-0.066000f,
	-1.940000f,1.950000f,-0.066000f,-1.940000f,2,0.066000f,-1.940000f,2,
	0.066000f,-1.840000f,2,-0.066000f,-1.940000f,2,0.066000f,-1.840000f,2,
	-0.066000f,-1.840000f,2,-0.066000f,-1.940000f,2,0.066000f,-1.940000f,
	1.950000f,0.066000f,-1.840000f,1.950000f,0.066000f,-1.840000f,2,0.066000f,
	-1.940000f,1.950000f,0.066000f,-1.840000f,2,0.066000f,-1.940000f,2,
	-0.066000f,-1.840000f,2,-0.171600f,-1.740000f,2,-0.066000f,-1.840000f,
	1.950000f,-0.171600f,-1.740000f,2,-0.171600f,-1.740000f,1.950000f,
	-0.066000f,-1.840000f,1.950000f,0.066000f,-1.840000f,1.950000f,0.171600f,
	-1.740000f,1.950000f,0.171600f,-1.740000f,2,0.066000f,-1.840000f,1.950000f,
	0.171600f,-1.740000f,2,0.066000f,-1.840000f,2,-0.171600f,-1.740000f,2,
	-0.188760f,-1.640000f,2,-0.188760f,-1.640000f,1.950000f,-0.188760f,
	-1.640000f,1.950000f,-0.171600f,-1.740000f,1.950000f,-0.171600f,-1.740000f,
	2,0.171600f,-1.740000f,1.950000f,0.188760f,-1.640000f,1.950000f,0.188760f,
	-1.640000f,2,0.171600f,-1.740000f,1.950000f,0.188760f,-1.640000f,2,
	0.171600f,-1.740000f,2,-0.188760f,-1.640000f,2,-0.132132f,-1.540000f,2,
	-0.132132f,-1.540000f,1.950000f,-0.132132f,-1.540000f,1.950000f,-0.188760f,
	-1.640000f,1.950000f,-0.188760f,-1.640000f,2,0.188760f,-1.640000f,
	1.950000f,0.132132f,-1.540000f,1.950000f,0.132132f,-1.540000f,2,0.188760f,
	-1.640000f,1.950000f,0.132132f,-1.540000f,2,0.188760f,-1.640000f,2,
	-0.132132f,-1.540000f,2,-0.052853f,-1.506390f,2,-0.052853f,-1.506390f,
	1.950000f,-0.052853f,-1.506390f,1.950000f,-0.132132f,-1.540000f,1.950000f,
	-0.132132f,-1.540000f,2,0.132132f,-1.540000f,1.950000f,0.052853f,
	-1.506390f,1.950000f,0.052853f,-1.506390f,2,0.132132f,-1.540000f,1.950000f,
	0.052853f,-1.506390f,2,0.132132f,-1.540000f,2,0.188760f,-1.640000f,
	1.950000f,0.173397f,-1.642679f,1.950000f,0.121808f,-1.551577f,1.950000f,
	0.121808f,-1.551577f,1.950000f,0.132132f,-1.540000f,1.950000f,0.188760f,
	-1.640000f,1.950000f,0.171600f,-1.740000f,1.950000f,0.157950f,-1.732697f,
	1.950000f,0.173397f,-1.642679f,1.950000f,0.171600f,-1.740000f,1.950000f,
	0.173397f,-1.642679f,1.950000f,0.188760f,-1.640000f,1.950000f,0.171600f,
	-1.740000f,1.950000f,0.066000f,-1.840000f,1.950000f,0.060149f,-1.825311f,
	1.950000f,0.171600f,-1.740000f,1.950000f,0.060149f,-1.825311f,1.950000f,
	0.157950f,-1.732697f,1.950000f,-0.066000f,-1.840000f,1.950000f,-0.060149f,
	-1.825311f,1.950000f,0.066000f,-1.840000f,1.950000f,-0.060149f,-1.825311f,
	1.950000f,0.060149f,-1.825311f,1.950000f,0.066000f,-1.840000f,1.950000f,
	-0.171600f,-1.740000f,1.950000f,-0.157950f,-1.732697f,1.950000f,-0.060149f,
	-1.825311f,1.950000f,-0.171600f,-1.740000f,1.950000f,-0.060149f,-1.825311f,
	1.950000f,-0.066000f,-1.840000f,1.950000f,-0.173397f,-1.642679f,1.950000f,
	-0.157950f,-1.732697f,1.950000f,-0.171600f,-1.740000f,1.950000f,-0.171600f,
	-1.740000f,1.950000f,-0.188760f,-1.640000f,1.950000f,-0.173397f,-1.642679f,
	1.950000f,-0.121808f,-1.551577f,1.950000f,-0.173397f,-1.642679f,1.950000f,
	-0.188760f,-1.640000f,1.950000f,-0.188760f,-1.640000f,1.950000f,-0.132132f,
	-1.540000f,1.950000f,-0.121808f,-1.551577f,1.950000f,-0.052853f,-1.506390f,
	1.950000f,-0.049868f,-1.521079f,1.950000f,-0.121808f,-1.551577f,1.950000f,
	-0.052853f,-1.506390f,1.950000f,-0.121808f,-1.551577f,1.950000f,-0.132132f,
	-1.540000f,1.950000f,0.049868f,-1.521079f,1.950000f,-0.049868f,-1.521079f,
	1.950000f,-0.052853f,-1.506390f,1.950000f,-0.052853f,-1.506390f,1.950000f,
	0.052853f,-1.506390f,1.950000f,0.049868f,-1.521079f,1.950000f,0.052853f,
	-1.506390f,1.950000f,0.132132f,-1.540000f,1.950000f,0.121808f,-1.551577f,
	1.950000f,0.052853f,-1.506390f,1.950000f,0.121808f,-1.551577f,1.950000f,
	0.049868f,-1.521079f,1.950000f,-0.188760f,-1.640000f,2,-0.173397f,
	-1.642679f,2,-0.121808f,-1.551577f,2,-0.121808f,-1.551577f,2,-0.132132f,
	-1.540000f,2,-0.188760f,-1.640000f,2,-0.171600f,-1.740000f,2,-0.157950f,
	-1.732697f,2,-0.173397f,-1.642679f,2,-0.173397f,-1.642679f,2,-0.188760f,
	-1.640000f,2,-0.171600f,-1.740000f,2,-0.066000f,-1.840000f,2,-0.060149f,
	-1.825311f,2,-0.171600f,-1.740000f,2,-0.060149f,-1.825311f,2,-0.157950f,
	-1.732697f,2,-0.171600f,-1.740000f,2,0.066000f,-1.840000f,2,0.060149f,
	-1.825311f,2,-0.066000f,-1.840000f,2,0.060149f,-1.825311f,2,-0.060149f,
	-1.825311f,2,-0.066000f,-1.840000f,2,0.171600f,-1.740000f,2,0.157950f,
	-1.732697f,2,0.060149f,-1.825311f,2,0.171600f,-1.740000f,2,0.060149f,
	-1.825311f,2,0.066000f,-1.840000f,2,0.173397f,-1.642679f,2,0.157950f,
	-1.732697f,2,0.171600f,-1.740000f,2,0.171600f,-1.740000f,2,0.188760f,
	-1.640000f,2,0.173397f,-1.642679f,2,0.121808f,-1.551577f,2,0.173397f,
	-1.642679f,2,0.188760f,-1.640000f,2,0.188760f,-1.640000f,2,0.132132f,
	-1.540000f,2,0.121808f,-1.551577f,2,0.052853f,-1.506390f,2,0.049868f,
	-1.521079f,2,0.121808f,-1.551577f,2,0.052853f,-1.506390f,2,0.121808f,
	-1.551577f,2,0.132132f,-1.540000f,2,-0.049868f,-1.521079f,2,0.049868f,
	-1.521079f,2,0.052853f,-1.506390f,2,0.052853f,-1.506390f,2,-0.052853f,
	-1.506390f,2,-0.049868f,-1.521079f,2,-0.121808f,-1.551577f,2,-0.049868f,
	-1.521079f,2,-0.052853f,-1.506390f,2,-0.052853f,-1.506390f,2,-0.132132f,
	-1.540000f,2,-0.121808f,-1.551577f,2,-0.173397f,-1.642679f,2,-0.157950f,
	-1.732697f,2,-0.157950f,-1.732697f,1.950000f,-0.157950f,-1.732697f,
	1.950000f,-0.173397f,-1.642679f,1.950000f,-0.173397f,-1.642679f,2,
	-0.157950f,-1.732697f,2,-0.060149f,-1.825311f,2,-0.060149f,-1.825311f,
	1.950000f,-0.060149f,-1.825311f,1.950000f,-0.157950f,-1.732697f,1.950000f,
	-0.157950f,-1.732697f,2,-0.060149f,-1.825311f,2,0.060149f,-1.825311f,2,
	0.060149f,-1.825311f,1.950000f,0.060149f,-1.825311f,1.950000f,-0.060149f,
	-1.825311f,1.950000f,-0.060149f,-1.825311f,2,0.060149f,-1.825311f,
	1.950000f,0.060149f,-1.825311f,2,0.157950f,-1.732697f,2,0.157950f,
	-1.732697f,2,0.157950f,-1.732697f,1.950000f,0.060149f,-1.825311f,1.950000f,
	0.157950f,-1.732697f,2,0.173397f,-1.642679f,2,0.173397f,-1.642679f,
	1.950000f,0.173397f,-1.642679f,1.950000f,0.157950f,-1.732697f,1.950000f,
	0.157950f,-1.732697f,2,0.173397f,-1.642679f,2,0.121808f,-1.551577f,2,
	0.121808f,-1.551577f,1.950000f,0.121808f,-1.551577f,1.950000f,0.173397f,
	-1.642679f,1.950000f,0.173397f,-1.642679f,2,0.121808f,-1.551577f,2,
	0.049868f,-1.521079f,2,0.049868f,-1.521079f,1.950000f,0.049868f,-1.521079f,
	1.950000f,0.121808f,-1.551577f,1.950000f,0.121808f,-1.551577f,2,0.049868f,
	-1.521079f,2,-0.049868f,-1.521079f,2,-0.049868f,-1.521079f,1.950000f,
	-0.049868f,-1.521079f,1.950000f,0.049868f,-1.521079f,1.950000f,0.049868f,
	-1.521079f,2,-0.049868f,-1.521079f,2,-0.121808f,-1.551577f,2,-0.121808f,
	-1.551577f,1.950000f,-0.121808f,-1.551577f,1.950000f,-0.049868f,-1.521079f,
	1.950000f,-0.049868f,-1.521079f,2,-0.121808f,-1.551577f,2,-0.173397f,
	-1.642679f,2,-0.173397f,-1.642679f,1.950000f,-0.173397f,-1.642679f,
	1.950000f,-0.121808f,-1.551577f,1.950000f,-0.121808f,-1.551577f,2,
	-0.360000f,3.600000f,0.100000f,0.360000f,3.600000f,0.100000f,4,4,0.100000f,
	4,4,0.100000f,-4,4,0.100000f,-0.360000f,3.600000f,0.100000f,-0.360000f,
	0.400000f,0.100000f,-0.360000f,3.600000f,0.100000f,-4,4,0.100000f,-4,4,
	0.100000f,-4,0,0.100000f,-0.360000f,0.400000f,0.100000f,4,0,0.100000f,
	0.360000f,0.400000f,0.100000f,-0.360000f,0.400000f,0.100000f,-0.360000f,
	0.400000f,0.100000f,-4,0,0.100000f,4,0,0.100000f,4,4,0.100000f,0.360000f,
	3.600000f,0.100000f,4,0,0.100000f,0.360000f,3.600000f,0.100000f,0.360000f,
	0.400000f,0.100000f,4,0,0.100000f,0.360000f,2.888889f,1.023752f,0.360000f,
	3.066667f,1.166974f,-0.360000f,3.066667f,1.166974f,-0.360000f,3.066667f,
	1.166974f,-0.360000f,2.888889f,1.023752f,0.360000f,2.888889f,1.023752f,
	0.360000f,2.533333f,0.939976f,0.360000f,2.711111f,0.966974f,-0.360000f,
	2.711111f,0.966974f,-0.360000f,2.711111f,0.966974f,-0.360000f,2.533333f,
	0.939976f,0.360000f,2.533333f,0.939976f,-0.360000f,2.177778f,0.939976f,
	0.360000f,2.177778f,0.939976f,0.360000f,2.355556f,0.939976f,0.360000f,
	2.355556f,0.939976f,-0.360000f,2.355556f,0.939976f,-0.360000f,2.177778f,
	0.939976f,-0.360000f,1.822222f,0.939976f,0.360000f,1.822222f,0.939976f,
	0.360000f,2,0.939976f,0.360000f,2,0.939976f,-0.360000f,2,0.939976f,
	-0.360000f,1.822222f,0.939976f,-0.360000f,1.466667f,0.939976f,0.360000f,
	1.466667f,0.939976f,0.360000f,1.644444f,0.939976f,0.360000f,1.644444f,
	0.939976f,-0.360000f,1.644444f,0.939976f,-0.360000f,1.466667f,0.939976f,
	0.360000f,1.111111f,0.957571f,0.360000f,1.288889f,0.939976f,-0.360000f,
	1.288889f,0.939976f,-0.360000f,1.288889f,0.939976f,-0.360000f,1.111111f,
	0.957571f,0.360000f,1.111111f,0.957571f,-0.360000f,0.755556f,1.134246f,
	0.360000f,0.755556f,1.134246f,0.360000f,0.933333f,1.009739f,0.360000f,
	0.933333f,1.009739f,-0.360000f,0.933333f,1.009739f,-0.360000f,0.755556f,
	1.134246f,0.360000f,0.755556f,1.134246f,-0.360000f,0.755556f,1.134246f,
	0.360000f,0.577778f,1.372130f,-0.360000f,0.755556f,1.134246f,-0.360000f,
	0.577778f,1.372130f,0.360000f,0.577778f,1.372130f,-0.360000f,3.600000f,
	3.900000f,-0.360000f,3.422222f,2.266974f,0.360000f,3.422222f,2.266974f,
	-0.360000f,3.600000f,3.900000f,0.360000f,3.422222f,2.266974f,0.360000f,
	3.600000f,3.900000f,0.360000f,3.244444f,1.466974f,-0.360000f,3.244444f,
	1.466974f,0.360000f,3.066667f,1.166974f,-0.360000f,3.244444f,1.466974f,
	-0.360000f,3.066667f,1.166974f,0.360000f,3.066667f,1.166974f,0.360000f,
	2.888889f,1.023752f,-0.360000f,2.888889f,1.023752f,0.360000f,2.711111f,
	0.966974f,-0.360000f,2.888889f,1.023752f,-0.360000f,2.711111f,0.966974f,
	0.360000f,2.711111f,0.966974f,0.360000f,2.533333f,0.939976f,-0.360000f,
	2.533333f,0.939976f,-0.360000f,2.355556f,0.939976f,0.360000f,2.533333f,
	0.939976f,-0.360000f,2.355556f,0.939976f,0.360000f,2.355556f,0.939976f,
	0.360000f,2.177778f,0.939976f,-0.360000f,2.177778f,0.939976f,-0.360000f,
	2,0.939976f,0.360000f,2.177778f,0.939976f,-0.360000f,2,0.939976f,0.360000f,
	2,0.939976f,0.360000f,1.822222f,0.939976f,-0.360000f,1.822222f,0.939976f,
	-0.360000f,1.644444f,0.939976f,0.360000f,1.822222f,0.939976f,-0.360000f,
	1.644444f,0.939976f,0.360000f,1.644444f,0.939976f,0.360000f,1.466667f,
	0.939976f,-0.360000f,1.466667f,0.939976f,-0.360000f,1.288889f,0.939976f,
	0.360000f,1.466667f,0.939976f,-0.360000f,1.288889f,0.939976f,0.360000f,
	1.288889f,0.939976f,0.360000f,1.111111f,0.957571f,-0.360000f,1.111111f,
	0.957571f,0.360000f,0.933333f,1.009739f,-0.360000f,1.111111f,0.957571f,
	-0.360000f,0.933333f,1.009739f,0.360000f,0.933333f,1.009739f,0.360000f,
	0.400000f,1.743932f,0.360000f,0.577778f,1.372130f,-0.360000f,0.577778f,
	1.372130f,-0.360000f,0.577778f,1.372130f,-0.360000f,0.400000f,1.743932f,
	0.360000f,0.400000f,1.743932f
	};

	static int[] world_indices = {
	0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,
	28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,
	53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,
	78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,
	102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,
	120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,
	138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,
	156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,
	174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,
	192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,
	210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,
	228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,
	246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,
	264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,279,280,281,
	282,283,284,285,286,287,288,289,290,291,292,293,294,295,296,297,298,299,
	300,301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317,
	318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,
	336,337,338,339,340,341,342,343,344,345,346,347,348,349,350,351,352,353,
	354,355,356,357,358,359,360,361,362,363,364,365,366,367,368,369,370,371,
	372,373,374,375,376,377,378,379,380,381,382,383,384,385,386,387,388,389,
	390,391,392,393,394,395,396,397,398,399,400,401,402,403,404,405,406,407,
	408,409,410,411,412,413,414,415,416,417,418,419,420,421,422,423,424,425,
	426,427,428,429,430,431,432,433,434,435,436,437,438,439,440,441,442,443,
	444,445,446,447,448,449,450,451,452,453,454,455,456,457,458,459,460,461,
	462,463,464,465,466,467,468,469,470,471,472,473,474,475,476,477,478,479,
	480,481,482,483,484,485
	};

}
