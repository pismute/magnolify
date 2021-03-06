/*
 * Copyright 2019 Spotify AB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package magnolify.cats.auto

import _root_.cats._
import cats.kernel.instances._

import scala.language.experimental.macros
import scala.reflect.macros._

private object CatsMacros {
  def genEqMacro[T: c.WeakTypeTag](c: whitebox.Context): c.Tree = {
    import c.universe._
    val wtt = weakTypeTag[T]
    q"""_root_.magnolify.cats.semiauto.EqDerivation.apply[$wtt]"""
  }

  def genHashMacro[T: c.WeakTypeTag](c: whitebox.Context): c.Tree = {
    import c.universe._
    val wtt = weakTypeTag[T]
    q"""_root_.magnolify.cats.semiauto.HashDerivation.apply[$wtt]"""
  }

  def genSemigroupMacro[T: c.WeakTypeTag](c: whitebox.Context): c.Tree = {
    import c.universe._
    val wtt = weakTypeTag[T]
    q"""_root_.magnolify.cats.semiauto.SemigroupDerivation.apply[$wtt]"""
  }

  def genMonoidMacro[T: c.WeakTypeTag](c: whitebox.Context): c.Tree = {
    import c.universe._
    val wtt = weakTypeTag[T]
    q"""_root_.magnolify.cats.semiauto.MonoidDerivation.apply[$wtt]"""
  }

  def genGroupMacro[T: c.WeakTypeTag](c: whitebox.Context): c.Tree = {
    import c.universe._
    val wtt = weakTypeTag[T]
    q"""_root_.magnolify.cats.semiauto.GroupDerivation.apply[$wtt]"""
  }
}

trait LowPriorityImplicits extends LowPriorityGenGroup {
  // more specific implicits to workaround ambiguous implicit values with cats
  implicit def genListMonoid[T] = new ListMonoid[T]
  implicit def genOptionMonoid[T: Semigroup] = new OptionMonoid[T]
  implicit def genListOrder[T: Order] = new ListOrder[T]
  implicit def genOptionOrder[T: Order] = new OptionOrder[T]
}

trait LowPriorityGenGroup extends LowPriorityGenMonoid {
  // more specific implicits to workaround ambiguous implicit values with cats
  implicit def genListHash[T: Hash] = new ListHash[T]
  implicit def genOptionHash[T: Hash] = new OptionHash[T]

  implicit def genHash[T]: Hash[T] = macro CatsMacros.genHashMacro[T]
  implicit def genGroup[T]: Group[T] = macro CatsMacros.genGroupMacro[T]
}

trait LowPriorityGenMonoid extends LowPriorityGenSemigroup {
  implicit def genEq[T]: Eq[T] = macro CatsMacros.genEqMacro[T]
  implicit def genMonoid[T]: Monoid[T] = macro CatsMacros.genMonoidMacro[T]
}

trait LowPriorityGenSemigroup {
  implicit def genSemigroup[T]: Semigroup[T] = macro CatsMacros.genSemigroupMacro[T]
}
